package com.group5.preppal.di;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.group5.preppal.viewmodel.CourseViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ViewModelComponent;
import dagger.hilt.android.internal.lifecycle.HiltViewModelFactory;
import dagger.multibindings.IntoMap;

@Module
@InstallIn(ViewModelComponent.class)  //
public abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(CourseViewModel.class)
    abstract ViewModel bindCourseViewModel(CourseViewModel courseViewModel);

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(HiltViewModelFactory factory);
}
