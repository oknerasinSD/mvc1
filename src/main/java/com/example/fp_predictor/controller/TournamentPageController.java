package com.example.fp_predictor.controller;

import com.example.fp_predictor.domain.Player;
import com.example.fp_predictor.domain.Tournament;
import com.example.fp_predictor.exceptions.UnknownLeagueException;
import com.example.fp_predictor.optimization.knapsack.FantasyTeam;
import com.example.fp_predictor.optimization.knapsack.dynamic.Dynamic;
import com.example.fp_predictor.optimization.stacks.TripleStack;
import com.example.fp_predictor.repository.PlayerRepository;
import com.example.fp_predictor.repository.TournamentRepository;
import com.example.fp_predictor.repository.TournamentTeamRepository;
import com.example.fp_predictor.scraping.League;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

@Controller
public class TournamentPageController {

    @Autowired
    private TournamentTeamRepository tournamentTeamRepository;

    @Autowired
    private PlayerRepository playerForecastRepository;

    @Autowired
    private TournamentRepository tournamentRepository;

    @Value("${upload.path}")
    private String uploadPath;

    @GetMapping("/tournament")
    public String open(@RequestParam("id") long id, Model model) {
        List<String> shortTeams = tournamentTeamRepository.findByTournamentId(id);
        List<String> teams = translateTeams(shortTeams);
        Collections.sort(teams);
        model.addAttribute("teams", shortTeams);
        return "tournamentPage";
    }

    private List<String> translateTeams(List<String> shortTeams) {
        List<String> teams = new ArrayList<>();
        for (String team : shortTeams) {
            switch (team) {
                case "ARS":
                    teams.add("Арсенал");
                    break;
                case "AV":
                    teams.add("Астон Вилла");
                    break;
                case "BHA":
                    teams.add("Брайтон");
                    break;
                case "BUR":
                    teams.add("Бернли");
                    break;
                case "CHE":
                    teams.add("Челси");
                    break;
                case "CRY":
                    teams.add("Кристал Пэлас");
                    break;
                case "EVE":
                    teams.add("Эвертон");
                    break;
                case "FUL":
                    teams.add("Фулхэм");
                    break;
                case "LEE":
                    teams.add("Лидс");
                    break;
                case "LEI":
                    teams.add("Лестер");
                    break;
                case "LIV":
                    teams.add("Ливерпуль");
                    break;
                case "MCI":
                    teams.add("Манчестер Сити");
                    break;
                case "MUN":
                    teams.add("Манчестер Юнайтед");
                    break;
                case "NEW":
                    teams.add("Ньюкасл");
                    break;
                case "SHU":
                    teams.add("Шеффилд");
                    break;
                case "SOU":
                    teams.add("Саутгемптон");
                    break;
                case "TOT":
                    teams.add("Тоттенхэм");
                    break;
                case "WBA":
                    teams.add("Вест Бром");
                    break;
                case "WHU":
                    teams.add("Вест Хэм");
                    break;
                case "WOL":
                    teams.add("Вулверхэмптон");
                    break;
            }
        }
        return teams;
    }

    @PostMapping("/tournament")
    public String count(
            @RequestParam("id") long id,
            @RequestParam(required = false) String team1,
            @RequestParam(required = false) String team2,
            @RequestParam(required = false) String team3,
            @RequestParam(required = false) String team4,
            Model model
    ) throws IOException {
        Tournament tournament = tournamentRepository.findById(id);
        Set<String> teams = new HashSet<>();
        if (team1 != null) {
            teams.add(team1);
        }
        if (team2 != null) {
            teams.add(team2);
        }
        if (team3 != null) {
            teams.add(team3);
        }
        if (team4 != null) {
            teams.add(team4);
        }
        Dynamic dynamic = new Dynamic(
                playerForecastRepository.findByTournamentId(id),
                getLeague(tournament),
                teams,
                tournament.getFanteam_id()
        );
        dynamic.solve();
        FantasyTeam finalTeam = dynamic.getFinalTeam();
        List<Player> players = extractPlayersByPositions(finalTeam);
        String filename = createCsvFile(
                players,
                tournament.getFanteam_id(),
                finalTeam.getCaptainId(),
                finalTeam.getViceCaptainId()
        );
        model.addAttribute("players", players);
        model.addAttribute("filename", filename);
        model.addAttribute("uploadPath", uploadPath);
        return "resultTeam";
    }

    /**
     * Создание csv-файла со сформированным составом для регистрации в турнир.
     * @param players - список игроков;
     * @param tournamentId - ID турнира в системе FanTeam;
     * @param captainId - ID капитана команды в системе FanTeam,
     * @param viceCaptainId - ID вице-капитана команды в системе FanTeam,
     * @return - название сформированного файла.
     * @throws IOException - ошибка записи данных в файл.
     */
    private String createCsvFile(List<Player> players, long tournamentId, long captainId, long viceCaptainId)
            throws IOException {
        String filename = UUID.randomUUID() + ".csv";
        File directory = new File(uploadPath + File.separator + "csv");
        File csvOutput = new File(directory + File.separator + filename);
        if (!directory.exists()) {
            directory.mkdir();
        }
        if (!csvOutput.exists()) {
            csvOutput.createNewFile();
        }
        FileWriter writer = new FileWriter(csvOutput);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(tournamentId).append(",");
        System.out.println(players.size());
        for (Player player : players) {
            stringBuilder.append(player.getFanteamPlayerId()).append(",");
        }
        stringBuilder.append(captainId).append(",").append(viceCaptainId);
        writer.write(stringBuilder.toString());
        writer.close();
        return filename;
    }

    private List<Player> extractPlayersByPositions(FantasyTeam team) {
        Map<String, List<Player>> playersByPositionMap = new HashMap<>(Map.of(
                "goalkeeper", new ArrayList<>(),
                "defender", new ArrayList<>(),
                "midfielder", new ArrayList<>(),
                "forward", new ArrayList<>()
        )
        );
        for (TripleStack stack : team.getTripleStacks()) {
            for (Player stackPlayer : stack.getPlayers()) {
                playersByPositionMap.get(stackPlayer.getPosition()).add(stackPlayer);
            }
        }
        for (Player stackPlayer : team.getDoubleStacks().get(0).getPlayers()) {
            playersByPositionMap.get(stackPlayer.getPosition()).add(stackPlayer);
        }
        List<Player> playersByPositionList = new ArrayList<>();
        playersByPositionList.addAll(playersByPositionMap.get("goalkeeper"));
        playersByPositionList.addAll(playersByPositionMap.get("defender"));
        playersByPositionList.addAll(playersByPositionMap.get("midfielder"));
        playersByPositionList.addAll(playersByPositionMap.get("forward"));
        return playersByPositionList;
    }

    private League getLeague(Tournament tournament) {
        switch (tournament.getLeague()) {
            case "England":
                return League.EPL;
            case "Spain":
                return League.LA_LIGA;
            case "Italy":
                return League.SERIE_A;
            default:
                throw new UnknownLeagueException();
        }
    }
}
