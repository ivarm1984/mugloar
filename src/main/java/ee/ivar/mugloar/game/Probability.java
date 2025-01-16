package ee.ivar.mugloar.game;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

@AllArgsConstructor
@Getter
@Log4j2
public enum Probability {
    HMM("Hmmm...."),
    PIECE_OF_CAKE("Piece of cake"),
    PLAYING_WITH_FIRE("Playing with fire"),
    RISKY("Risky"),
    SURE_THING("Sure thing"),
    WALK_IN_THE_PARK("Walk in the park"),
    QUITE_LIKELY("Quite likely"),
    SUICIDE_MISSION("Suicide mission"),
    GAMBLE("Gamble"),
    RATHER_DETRIMENTAL("Rather detrimental"),
    UNKNOWN("");

    private final String probability;

    public static Probability fromString(String probability) {
        for (Probability prob : values()) {
            if (prob.getProbability().equalsIgnoreCase(probability)) {
                return prob;
            }
        }
        log.warn("Unknown probability {}", probability);
        return UNKNOWN;
    }
}
