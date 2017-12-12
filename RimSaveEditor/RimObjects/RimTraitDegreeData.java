package RimSaveEditor.RimObjects;

import java.util.HashMap;
import java.util.Map;

public class RimTraitDegreeData {
    public String label;
    public String description;
    public Map<String, String> statOffsets = new HashMap<>();
    public Map<String, Integer> skillGains = new HashMap<>();
    public int degree;
}
