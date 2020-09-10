package com.neo.notekeeperjavarevision;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;


/**
 * class for courses obj
 */
public final class CourseInfo implements Parcelable {
    private final String mCourseId;
    private final String mTitle;
    private final List<ModuleInfo> mModules;

    public CourseInfo(String courseId, String title, List<ModuleInfo> modules) {
        mCourseId = courseId;
        mTitle = title;
        mModules = modules;
    }

    private CourseInfo(Parcel source) {
        mCourseId = source.readString();
        mTitle = source.readString();
        mModules = new ArrayList<>();
        source.readTypedList(mModules, ModuleInfo.CREATOR);
    }

    public String getCourseId() {
        return mCourseId;
    }

    public String getTitle() {
        return mTitle;
    }

    public List<ModuleInfo> getModules() {
        return mModules;
    }

    public boolean[] getModulesCompletionStatus() {
        boolean[] status = new boolean[mModules.size()];

        for(int i=0; i < mModules.size(); i++)
            status[i] = mModules.get(i).isComplete();

        return status;
    }

    public void setModulesCompletionStatus(boolean[] status) {
        for(int i=0; i < mModules.size(); i++)
            mModules.get(i).setComplete(status[i]);
    }

    public ModuleInfo getModule(String moduleId) {
        for(ModuleInfo moduleInfo: mModules) {
            if(moduleId.equals(moduleInfo.getModuleId()))
                return moduleInfo;
        }
        return null;
    }

    @Override
    public String toString() {
        // what is shown on spinner holding course obj i.e to the spinner array adapter
        return mTitle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CourseInfo that = (CourseInfo) o;

        return mCourseId.equals(that.mCourseId);

    }

    @Override
    public int hashCode() {
        return mCourseId.hashCode();
    }


    @Override
    // to describe any special behaviour parcelling might require
    public int describeContents() {
        return 0;
    }

    @Override
    // receives ref to parcel instance and writes the data in the parcel
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mCourseId);
        dest.writeString(mTitle);
        dest.writeTypedList(mModules);
    }


    // implementation of interface Parcelable.Create to make class re-creatable from a parcel
    public static final Creator<CourseInfo> CREATOR =
            new Creator<CourseInfo>() {

                @Override
                // creates a new instance of Parcel class, init it from parcel that has been written to
                public CourseInfo createFromParcel(Parcel source) {
                    return new CourseInfo(source);
                }

                @Override
                // responsible to create an array of the parcel Class and of a specified size
                public CourseInfo[] newArray(int size) {
                    return new CourseInfo[size];
                }
            };

}
