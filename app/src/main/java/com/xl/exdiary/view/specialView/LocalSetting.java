package com.xl.exdiary.view.specialView;

import java.io.FileDescriptor;
import java.io.Serializable;

public class LocalSetting implements Serializable {
    public boolean isNoBackground = false;
    public String settingBackground = null;
    public String mainBackground = null;
    public String friendBackground = null;
    public LocalSetting(boolean isNoBackground){
        this.isNoBackground = isNoBackground;
    }
}
