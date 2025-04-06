package com.group5.preppal.data.model.test.listening;

import com.google.firebase.firestore.PropertyName;

public class ListeningSection {
    private ListeningPart part1;
    private ListeningPart part2;
    private ListeningPart part3;
    private ListeningPart part4;

    public ListeningSection() {}

    @PropertyName("part_1")
    public ListeningPart getPart1() {
        return part1;
    }

    @PropertyName("part_1")
    public void setPart1(ListeningPart part1) {
        this.part1 = part1;
    }

    @PropertyName("part_2")
    public ListeningPart getPart2() {
        return part2;
    }

    @PropertyName("part_2")
    public void setPart2(ListeningPart part2) {
        this.part2 = part2;
    }

    @PropertyName("part_3")
    public ListeningPart getPart3() {
        return part3;
    }

    @PropertyName("part_3")
    public void setPart3(ListeningPart part3) {
        this.part3 = part3;
    }

    @PropertyName("part_4")
    public ListeningPart getPart4() {
        return part4;
    }

    @PropertyName("part_4")
    public void setPart4(ListeningPart part4) {
        this.part4 = part4;
    }
}
