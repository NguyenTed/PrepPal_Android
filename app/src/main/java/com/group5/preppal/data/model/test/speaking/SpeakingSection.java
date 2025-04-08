package com.group5.preppal.data.model.test.speaking;

import com.google.firebase.firestore.PropertyName;

import java.util.List;

public class SpeakingSection {
    private List<String> part1;
    private SpeakingPartTwo part2;
    private List<String> part3;

    public SpeakingSection() {}

    @PropertyName("part_1")
    public List<String> getPart1() {
        return part1;
    }

    @PropertyName("part_1")
    public void setPart1(List<String> part1) {
        this.part1 = part1;
    }

    @PropertyName("part_2")
    public SpeakingPartTwo getPart2() {
        return part2;
    }

    @PropertyName("part_2")
    public void setPart2(SpeakingPartTwo part2) {
        this.part2 = part2;
    }

    @PropertyName("part_3")
    public List<String> getPart3() {
        return part3;
    }

    @PropertyName("part_3")
    public void setPart3(List<String> part3) {
        this.part3 = part3;
    }
}

