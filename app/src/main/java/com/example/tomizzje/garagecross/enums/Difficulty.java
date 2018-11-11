package com.example.tomizzje.garagecross.enums;

public enum Difficulty {
    BEGINNER(0){
        @Override
        public String getUserFriendlyString(){
            return "kezdő";
        }
    },INTERMEDIATE(1) {
        @Override
        public String getUserFriendlyString() {
            return "középhaladó";
        }

    },ADVANCED(2){
        @Override
        public String getUserFriendlyString() {
            return "haladó";
        }
    },PROFESSIONAL(3){
        @Override
        public String getUserFriendlyString(){
            return "profi";
        }
    };

    private final int difficultyCode;

    private Difficulty(int difficultyCode){
        this.difficultyCode = difficultyCode;
    }

    public int getDifficultyCode(){
        return this.difficultyCode;
    }

    public static Difficulty getDifficulty(int difficultyCode){
        for(Difficulty d : Difficulty.values()){
            if(d.difficultyCode == difficultyCode){
                return d;
            }
        }
        return null;
    }

    public static int getDifficultyCode(String name){
        Difficulty[] temp = Difficulty.values();
        for(int i=0;i<temp.length;++i){
            if(temp[i].toString().equals(name)){
                return i;
            }
        }
        return -1;
    }

    public static int getDifficultyPoints(String difficulty){
        switch(difficulty){
            case "BEGINNER":
                return 1;
            case "INTERMEDIATE":
                return 2;
            case "ADVANCED":
                return 5;
            default:
                return 10;
        }
    }

    public static String getDifficultyString(String difficulty){
        switch(difficulty){
            case "BEGINNER":
                return "kezdő";
            case "INTERMEDIATE":
                return "középhaladó";
            case "ADVANCED":
                return "haladó";
            default:
                return "profi";
        }
    }


    public abstract String getUserFriendlyString();
}
