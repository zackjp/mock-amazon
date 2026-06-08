pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Mock Amazon"
include(":app")
include(":core:analytics:api")
include(":core:analytics:impl")
include(":feature:checkout")
include(":feature:home")
include(":lib:shared")
include(":lib:shared-test-utils")
include(":feature:product")
include(":feature:cart")
include(":feature:search")
