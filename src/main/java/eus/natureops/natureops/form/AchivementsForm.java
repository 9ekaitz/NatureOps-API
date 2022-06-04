package eus.natureops.natureops.form;

import eus.natureops.natureops.domain.Achivement;
import lombok.Data;

@Data
public class AchivementsForm {
    Achivement achivement;
    int progress;

    public Achivement getAchivement() {
        return achivement;
    }
    public void setAchivement(Achivement achivement) {
        this.achivement = achivement;
    }
    public int getProgress() {
        return progress;
    }
    public void setProgress(int progress) {
        this.progress = progress;
    } 
}
