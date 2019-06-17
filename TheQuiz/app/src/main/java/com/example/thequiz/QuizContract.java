package com.example.thequiz;

import android.provider.BaseColumns;

public final class QuizContract {

    private QuizContract(){}

    public static class CategorieTabel implements BaseColumns {
        public static final String TABLE_NAME = "quiz_categories";
        public static final String COLUMN_NAME = "naam";
    }

    public static class VragenTabel implements BaseColumns {
        public static final String TABLE_NAME = "quiz_vragen";
        public static final String COLUMN_VRAAG = "vraag";
        public static final String COLUMN_OPTIE1 = "optie1";
        public static final String COLUMN_OPTIE2 = "optie2";
        public static final String COLUMN_OPTIE3 = "optie3";
        public static final String COLUMN_ANTW_NR = "antwoord_nr";
        public static final String COLUMN_MOEILIJKHEID = "moeilijkheid";
        public static final String COLUMN_CATEGORIE_ID = "categorie_id";
    }
}
