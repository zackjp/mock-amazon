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

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "mock-amazon"
include(":app")
include(":core:analytics:api")
include(":core:analytics:impl")
include(":core:data")
include(":core:model")
include(":feature:checkout")
include(":feature:home")
include(":lib:shared")
include(":lib:shared-test-utils")
include(":feature:product")
include(":feature:cart")
include(":feature:search")
