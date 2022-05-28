package com.github.strindberg.emacssearchandcase.services

import com.intellij.openapi.project.Project
import com.github.strindberg.emacssearchandcase.MyBundle

class MyProjectService(project: Project) {

    init {
        println(MyBundle.message("projectService", project.name))
    }
}
