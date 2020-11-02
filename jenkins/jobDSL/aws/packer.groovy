def images =  [
    "jenkins-master",
    "vault",
]

folder("Packer")
folder("Packer/aws")
for(image in images) {
     pipelineJob("Packer/aws/${image}") {
        parameters {
            gitParameter {
                name('revision')
                type('PT_BRANCH_TAG')
                sortMode('ASCENDING_SMART')
                defaultValue('origin/master')
                selectedValue('DEFAULT')
                description('')
                branch('')
                branchFilter('')
                tagFilter('')
                useRepository('')
                quickFilterEnabled(true)
            }
        }

        logRotator {
            numToKeep(50)
        }

        definition {
            cpsScm {
                scm {
                    git {
                        remote {
                            github("fr123k/packer-images", "ssh")
                            credentials("deploy-key-shared-library")
                        }

                        branch('$revision')
                    }
                }

                scriptPath("aws/${image}/Jenkinsfile")
            }
        }
    }
    // multibranchPipelineJob("Packer/aws/${image}") {
    //     orphanedItemStrategy {
    //         discardOldItems {
    //             numToKeep(0)
    //         }
    //     }

    //     description("Builds for packer-images")

    //     branchSources {
    //         // Adds a GitHub branch source.
    //         github {
    //             // Specifies a unique ID for this branch source.
    //             id("packer-images-branch-id")
    //             // Sets checkout credentials for authentication with GitHub.
    //             checkoutCredentialsId("deploy-key-shared-library")
    //             // Sets the name of the GitHub Organization or GitHub User Account.
    //             repoOwner("fr123k")
    //             // Sets the name of the GitHub repository.
    //             repository("packer-images")
    //             // Sets scan credentials for authentication with GitHub.
    //             //scanCredentialsId(String scanCredentialsId)
    //         }
    //     }

    //     configure { project ->
    //         project / 'sources' / 'data' / 'jenkins.branch.BranchSource'/ strategy(class: 'jenkins.branch.DefaultBranchPropertyStrategy') {
    //             properties(class: 'java.util.Arrays$ArrayList') {
    //                 a(class: 'jenkins.branch.BranchProperty-array'){
    //                     'jenkins.branch.NoTriggerBranchProperty'()
    //                 }
    //             }
    //         }
    //     }


    //     factory {
    //         workflowBranchProjectFactory {
    //             scriptPath("aws/${image}/Jenkinsfile")
    //         }
    //     }

    //     // trigger the repository scan once a day to delete stale jobs
    //     triggers {
    //         periodicFolderTrigger {
    //             interval('1d')
    //         }
    //     }
    // }
}
