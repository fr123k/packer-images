def images =  [
    "jenkins-master",
    "vault",
]

folder("Packer")
folder("Packer/aws")
for(image in images) {
    multibranchPipelineJob("Packer/aws/${image}") {
        orphanedItemStrategy {
            discardOldItems {
                numToKeep(0)
            }
        }

        description("Builds for packer-images")

        branchSources {
            // Adds a GitHub branch source.
            github {
                // Specifies a unique ID for this branch source.
                id("packer-images-branch-id")
                // Sets checkout credentials for authentication with GitHub.
                checkoutCredentialsId("deploy-key-shared-library")
                // Sets the name of the GitHub Organization or GitHub User Account.
                repoOwner("fr123k")
                // Sets the name of the GitHub repository.
                repository("packer-images")
                // Sets scan credentials for authentication with GitHub.
                //scanCredentialsId(String scanCredentialsId)
            }
        }

        configure { project ->
            project / 'sources' / 'data' / 'jenkins.branch.BranchSource'/ strategy(class: 'jenkins.branch.DefaultBranchPropertyStrategy') {
                properties(class: 'java.util.Arrays$ArrayList') {
                    a(class: 'jenkins.branch.BranchProperty-array'){
                        'jenkins.branch.NoTriggerBranchProperty'()
                    }
                }
            }
        }


        factory {
            workflowBranchProjectFactory {
                scriptPath("aws/${image}/Jenkinsfile")
            }
        }

        // trigger the repository scan once a day to delete stale jobs
        triggers {
            periodicFolderTrigger {
                interval('1d')
            }
        }
    }
}
