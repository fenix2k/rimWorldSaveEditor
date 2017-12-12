package RimSaveEditor.RimObjects;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RimApparel {
    public String def = "";
    public String id = "";
    public String label = "";
    public String description = "";
    public int health = 500;
    public String stuff = "Cloth";
    public int stackCount = 1;
    public String color = "RGBA(0.400, 0.300, 0.150, 1.000)";
    public String colorActive = "True";

    public RimItemQuality quality = RimItemQuality.Normal;
    public RimApparel defLink = null;

    public List<RimApparelBodyParts> bodyPartGroups = new ArrayList<>();
    public List<RimApparelLayers> layers = new ArrayList<>();

    @Override
    public String toString() {
        if(!def.isEmpty()) {
            if (!label.isEmpty())
                return def + " (" + label + ")";
            else return def;
        }
        else return "none";
    }

    public RimApparel() {
    }

    public void setQuality(String quality) {
        if(!quality.isEmpty()) this.quality = RimItemQuality.valueOf(quality);
        else this.quality = RimItemQuality.Normal;
    }

    public String getQuality() {
        return this.quality.name();
    }

    public void addBodyPart(String bodyPart) {
        if(!bodyPart.isEmpty()) this.bodyPartGroups.add(RimApparelBodyParts.valueOf(bodyPart));

    }public void addLayer(String layer) {
        if(!layer.isEmpty()) this.layers.add(RimApparelLayers.valueOf(layer));
    }
}
