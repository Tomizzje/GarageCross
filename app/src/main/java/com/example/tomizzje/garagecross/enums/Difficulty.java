package com.example.tomizzje.garagecross.enums;

public enum Difficulty {
    BEGINNER("kezdő"),
    INTERMEDIATE("középhaladó"),
    ADVANCED("haladó"),
    PROFESSIONAL("profi");

    private final String userFriendlyText;

    Difficulty(String userFriendlyText){
        this.userFriendlyText = userFriendlyText;
    }

    @Override public String toString(){
        return userFriendlyText;
    }

    /**
     * This method returns enum by his friendlyText
     * @param userFriendlyText Difficulty friendlytext
     * @return Difficulty
     */
    public static Difficulty getDifficultyByString(String userFriendlyText){
        for(Difficulty d : Difficulty.values()){
            if(d.userFriendlyText.equals(userFriendlyText)){
                return d;
            }
        }
        return BEGINNER;
    }

    /**
     * return Difficulty by his name
     * @param text name of difficulty
     * @return difficulty
     */
    public static Difficulty getDifficultyByName(String text){
        for(Difficulty d : Difficulty.values()){
            if(d.name().equals(text)){
                return d;
            }
        }
        return BEGINNER;
    }

    /**
     * This method return point by difficulty
     * @param difficulty enum
     * @return point
     */
    public static int getDifficultyPoints(Difficulty difficulty){
        switch(difficulty){
            case BEGINNER:
                return 1;
            case INTERMEDIATE:
                return 2;
            case ADVANCED:
                return 5;
            case PROFESSIONAL:
                return 10;
            default:
                return 1;
        }
    }

    /**
     * This method return a string array of difficulty enum
     * @return String array
     */
    public static String[] getDifficultyValuesString(){
        Difficulty[] temp = Difficulty.values();
        String[] result = new String[temp.length];
        for(int i=0;i<temp.length;++i){
            result[i] = temp[i].toString();
        }
        return result;
    }

    /**
     * This method select the user experience by his points
     * @param experience user's experience
     * @return Difficulty
     */
    public static Difficulty getDifficultyLevelByExperience(int experience) {
       if(experience < 50){
           return Difficulty.BEGINNER;
       } else if( experience < 150) {
           return Difficulty.INTERMEDIATE;
       } else if(experience < 300){
           return Difficulty.ADVANCED;
       } else {
           return Difficulty.PROFESSIONAL;
       }
    }

    /**
     * This method return a one more stronger difficulty
     * @param d Difficulty of the user
     * @return A stronger difficulty
     */
    public static Difficulty getStrongerDifficultyLevel(Difficulty d) {
        for(int i=0;i<Difficulty.values().length;++i){
            if(values()[i].name().equals(d.name()) && i != Difficulty.values().length -1 ){
                return Difficulty.values()[i+1];
            }
        }
        return Difficulty.values()[Difficulty.values().length-1];
    }
}
