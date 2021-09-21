# Contributing to HardcoreLife

:+1::tada: First off, thanks for taking the time to contribute! :tada::+1:

The following is a set of guidelines for contributing to HardcoreLife. These are mostly guidelines, not rules. Use your best judgment, and feel free to propose changes to this document in a pull request.

## Table Of Contents

[Code of Conduct](#code-of-conduct)

[I don't want to read this whole thing, I just have a question!!!](#i-dont-want-to-read-this-whole-thing-i-just-have-a-question)

- [HardcoreLife Design Decisions](#design-decisions)

[How Can I Contribute?](#how-can-i-contribute)

- [Reporting Bugs](#reporting-bugs)
- [Suggesting Enhancements](#suggesting-enhancements)
- [Your First Code Contribution](#your-first-code-contribution)
- [Pull Requests](#pull-requests)

[Styleguides](#styleguides)

- [Git Commit Messages](#git-commit-messages)
- [Java Styleguide](#java-styleguide)
- [Documentation Styleguide](#documentation-styleguide)

[Additional Notes](#additional-notes)

- [Issue and Pull Request Labels](#issue-and-pull-request-labels)

## Code of Conduct

This project and everyone participating in it is expected to be respectful of eachother.

## I don't want to read this whole thing I just have a question!

Please feel free to open an issue or add a comment to any of our 3rd party postings, linked in [README.md](./README.md).

### Design Decisions

When we make a significant decision in how we maintain the project and what we can or cannot support, we will document it in this section. If you have a question around how we do things, check to see if it is documented here. If it is _not_ documented, please open a new issue and ask your question.

## How Can I Contribute?

### Reporting Bugs

Before Submitting A Bug Report **Perform a search** to see if the problem has already been reported. If it has **and the issue is still open**, add a comment to the existing issue instead of opening a new one.

> **Note:** If you find a **Closed** issue that seems like it is the same thing that you're experiencing, open a new issue and include a link to the original issue in the body of your new one.

#### How Do I Submit A (Good) Bug Report?

Bugs are tracked as [GitHub issues](https://guides.github.com/features/issues/). Create an issue and provide the following information by filling in [the template](https://github.com/Chryscorelab/HardcoreLife/blob/master/.github/ISSUE_TEMPLATE/bug_report.md).

Explain the problem and include additional details to help maintainers reproduce the problem:

- Use a clear and descriptive title for the issue to identify the problem.
- Describe the exact steps which reproduce the problem in as many details as possible. When listing steps, don't just say what you did, but explain how you did it. For example, if you ran a command, let us know if this was ran form the server console or as an in-game player.
- Provide specific examples to demonstrate the steps.
- Describe the behavior you observed after following the steps and point out what exactly is the problem with that behavior.
- Explain which behavior you expected to see instead and why.
- If you're reporting that HardcoreLife crashed, include a crash report and server logs to show the crash and any errors.
- If the problem wasn't triggered by a specific action, describe what you were doing before the problem happened and share more information using the guidelines below.

Provide more context by answering these questions:

- Did the problem start happening recently (e.g. after updating to a new version of HardcoreLife or installing a new plugin) or was this always a problem?
- If the problem started happening recently, can you reproduce the problem in an older version of HardcoreLife? What's the most recent version in which the problem doesn't happen?
- Can you reliably reproduce the issue? If not, provide details about how often the problem happens and under which conditions it normally happens.
- Does the problem happen for all players or only some?

Include details about your configuration and environment:

- Which version of HardcoreLife are you using?
- What verion of PaperMC are you running?
- What config.yml settings do you have?
- What other plugins do you have installed?

### Suggesting Enhancements

Enhancements include completely new features and minor improvements to existing functionality. Following these guidelines helps maintainers and the community understand your suggestion :pencil: and find related suggestions :mag_right:.

Before creating enhancement suggestions, please check [this list](#before-submitting-an-enhancement-suggestion) as you might find out that you don't need to create one. When you are creating an enhancement suggestion, please [include as many details as possible](#how-do-i-submit-a-good-enhancement-suggestion). Fill in [the template](https://github.com/Chryscorelab/HardcoreLife/blob/master/.github/ISSUE_TEMPLATE/feature_request.md), including the steps that you imagine you would take if the feature you're requesting existed.

#### Before Submitting An Enhancement Suggestion

Before Submitting An Enhancement Suggestion, perform a search to see if the enhancement has already been suggested. If it has, add a comment to the existing issue instead of opening a new one.

#### How Do I Submit A (Good) Enhancement Suggestion?

Enhancement suggestions are tracked as [GitHub issues](https://guides.github.com/features/issues/). We ask that you provide the following information in your issue via [the template](https://github.com/Chryscorelab/HardcoreLife/blob/master/.github/ISSUE_TEMPLATE/feature_request.md):

- Use a clear and descriptive title for the issue to identify the suggestion.
- Provide a step-by-step description of the suggested enhancement in as many details as possible.
- Provide specific examples to demonstrate the steps\*\*. Let us know how you and others might use your feature.
- Describe the current behavior and explain which behavior you expected to see instead and why.
- Explain why this enhancement would be useful to other HardcoreLife users.
- List any examples where this enhancement exists.
- Specify which version of HardcoreLife you're using. This can be found by viewing your server's `plugin` folder.

### Your First Code Contribution

Unsure where to begin contributing to HardcoreLife? You can start by looking through these `beginner` and `help-wanted` issues:

- [Beginner issues][beginner] - issues which should only require a few lines of code, and a test or two.
- [Help wanted issues][help-wanted] - issues which should be a bit more involved than `beginner` issues.

Both issue lists are sorted by total number of comments. While not perfect, number of comments is a reasonable proxy for impact a given change will have.

#### Local development

The HardcoreLife plugin can be developed locally. You will require the Java version and Maven version noted in [pom.xml](./pom.xml).

### Pull Requests

The process described here has several goals:

- Fix problems that are important to users
- Enable a sustainable system for HardcoreLifes's maintainers to review contributions

Please follow these steps to have your contribution considered by the maintainers:

1. Follow all instructions in [the template](PULL_REQUEST_TEMPLATE.md)
2. Follow the [styleguides](#styleguides)
3. After you submit your pull request, verify that all [status checks](https://help.github.com/articles/about-status-checks/) are passing <details><summary>What if the status checks are failing?</summary>If a status check is failing, and you believe that the failure is unrelated to your change, please leave a comment on the pull request explaining why you believe the failure is unrelated. A maintainer will re-run the status check for you. If we conclude that the failure was a false positive, then we will open an issue to track that problem with our status checks.</details>

While the prerequisites above must be satisfied prior to having your pull request reviewed, the reviewer(s) may ask you to complete additional design work, tests, or other changes before your pull request can be ultimately accepted.

## Styleguides

### Git Commit Messages

- Use the present tense ("Add feature" not "Added feature")
- Use the imperative mood ("Move cursor to..." not "Moves cursor to...")
- Limit the first line to 72 characters or less

### Java Styleguide

All Java code should be linted with [Prettier](https://prettier.io/) before comitting.

- Names should follow the standard [naming conventions](https://www.javatpoint.com/java-naming-conventions).
- Commented code blocks should be removed before merging.
- Any unneeded imports should be omitted.
- Attempt to avoid unnecessary variables

  ```java
    // Use this:
    logger.info("Deleting: " + world.getName());

    // Instead of:
    String worldName = world.getName();
    logger.info("Deleting: " + worldName);
  ```

### Documentation Styleguide

- Use [Markdown](https://daringfireball.net/projects/markdown).
  - Markdown files should attempt to follow the style of [README.md](./README.md) and the existing Wiki.
- Methods and classes should have comments noting their purpose.
- If code is not clear to read, we reccommend adding a comment to note it's usage. It will help us down the road!

## Additional Notes

### Issues and Projects

The core contriubutors may assign an issue to an active [project](https://github.com/Chryscorelab/HardcoreLife/projects). This normally means we're actively considering the issue and how to resolve it. Conversation arount the issue should be contained to the related issue, including status updates and implementation questions.
