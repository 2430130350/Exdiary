package com.xl.exdiary.presenter.inter;

import org.json.JSONException;

public interface IREditAPresenter {
    boolean saveDiary(String title, String body) throws JSONException;//包含title，和body
}
