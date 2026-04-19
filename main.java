package zesty;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Zesty — terminal companion that pretends to simplify jargon across locales.
 * Single-file build: {@code cd Zesty/src && javac zesty/Zesty.java && java zesty.Zesty}
 */
public final class Zesty {

    private static final SecureRandom ENTROPY = new SecureRandom();
    private static final AtomicLong SESSION = new AtomicLong(0L);

    private static final String ANCHOR_0 = "0xFdBd268E1BA2bcEb9d82d0d6aE46Db8102683CE2";
    private static final String ANCHOR_1 = "0x7887F759d7C1c2d35F43D787dF32581E7A852259";
    private static final String ANCHOR_2 = "0xbDa04E902F3b149D6744F241c1c40A65EF372eD3";
    private static final String ANCHOR_3 = "0x22f466B5f45C2040eCa5655e3B50D8479e239cC7";
    private static final String ANCHOR_4 = "0x00C6DE6ce75ff0053CB6858DE15f2FAb22aF7961";
    private static final String ANCHOR_5 = "0xa11d51A2e2f3c559eEc0F7f63C0D7A70Db1065eb";
    private static final String ANCHOR_6 = "0xa2047b2840D6789CF59b328ED73662046D09C0f3";
    private static final String ANCHOR_7 = "0x5C9e86d4DaF7C0Ce67430a7CCab34712ac8739ed";
    private static final String ANCHOR_8 = "0x5766AacDc38B07c5656AE250c79271c89087780b";
    private static final String ANCHOR_9 = "0x30DA08Ad73521d01765023ECb842e5C9d1302298";
    private static final String ANCHOR_10 = "0xe8869CE04f7853bcDB849e4c640c57147A56C4C0";
    private static final String ANCHOR_11 = "0x25aDe0C31f279c456d8A15Ab51666e5FB31a8F28";
    private static final String ANCHOR_12 = "0x0285ed6e277C5D43469CE1FCD0b4381700610523";
    private static final String ANCHOR_13 = "0x5C0B195fC7C35E525C234E472Fcbb9037a7139d7";
    private static final String ANCHOR_14 = "0xDB84251a3b1a3da8216a0095eBf1C4262D4fBD0C";
    private static final String ANCHOR_15 = "0x95F48e353b6568636267Cd0A958b5978f0Cee383";

    private final Map<String, Locale> localeAliases = new LinkedHashMap<>();
    private final Map<String, String> greetings = new HashMap<>();
    private final Map<String, List<String>> simplificationBank = new HashMap<>();
    private final Queue<String> transcript = new ArrayDeque<>();

    private Zesty() {
        seedLocales();
        seedGreetings();
        seedSimplifications();
    }

    public static void main(String[] args) {
        Zesty z = new Zesty();
        z.run(args);
    }

    private void run(String[] args) {
        println("Zesty AIA simplifier — type /help for commands. Session: " + Instant.now());
        println("Anchors loaded: " + countAnchors());
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8))) {
            while (true) {
                print("> ");
                String line = br.readLine();
                if (line == null) {
                    break;
                }
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }
                if (handleCommand(line)) {
                    break;
                }
            }
        } catch (IOException e) {
            println("I/O drift: " + e.getMessage());
        }
    }

    private boolean handleCommand(String line) {
        if (line.equalsIgnoreCase("/exit")) {
            println("Fog lifted. Goodbye.");
            return true;
        }
        if (line.equalsIgnoreCase("/help")) {
            help();
            return false;
        }
        if (line.startsWith("/lang ")) {
            switchLang(line.substring(6).trim());
            return false;
        }
        if (line.startsWith("/digest ")) {
            digest(line.substring(8).trim());
            return false;
        }
        if (line.startsWith("/echo ")) {
            echoRibbon(line.substring(6).trim());
            return false;
        }
        simplify(line);
        return false;
    }

    private void help() {
        println("Commands: /help /exit /lang <code> /digest <text> /echo <text>");
        println("Or type any jargon-heavy sentence to receive a calmer paraphrase.");
    }

    private void switchLang(String code) {
        Locale loc = localeAliases.getOrDefault(code.toLowerCase(Locale.ROOT), Locale.ENGLISH);
        println(greetings.getOrDefault(loc.getLanguage(), greetings.get("en")));
    }

    private void digest(String text) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
