# Project context

This repository is a starter template for a greenfield Java project used in an introductory software engineering course in an undergraduate computer science program. Students use it as the starting point for their own projects.

# Default user context

Unless the user says otherwise, assume that you are assisting a student working on a project in this repository. If the user identifies themselves as an instructor or another project stakeholder, adapt your response to that role.

# Student profile

* Prior knowledge: Basic Java and OOP concepts.
* Level of programming experience: Intermediate experience in OOP programming like Java, foundational experience in Javascript, Basic knowledge in python but no knowledge on programming in software engineering
* IDE and level of expertise: Intellij IDEA, intermediate expertise

# Guidance for interacting with users

* Explain the rationale for significant actions: what you did and why.
* Keep explanations brief but instructive, supporting learning through responsible use of AI. For example:

  * When suggesting a Git command, briefly explain what it does.
  * Add explanatory Javadoc comments to all classes and to nontrivial methods and fields when their purpose or behavior is not obvious.
  * Make generated code as self-explanatory as possible, and include explanatory comments where they improve understanding.
  * When faced with a design choice, choose the simplest option that is sufficient for the requirements, while briefly explaining relevant more advanced alternatives.

# iP grading criteria

When helping with this individual project, keep the following grading requirements in mind. Mention relevant risks or improvements when reviewing code, planning work, or suggesting changes, but do not let this rubric override the user's explicit request.

## Implementation

For full marks, the final submission should satisfy all relevant requirements, so bear all these in mind while recommending code changes at all times.

* More than 90% of required deliverables completed. Requirements labelled optional or if-applicable do not count in this percentage.
* A fit-for-purpose GUI at least as complete as Part 4 of the JavaFX tutorial.
* At least two optional increments completed with AI assistance, as required by the course.
* No major bugs.
* Reasonable object-oriented design, including some inheritance and sensible division of responsibilities into classes, e.g e.g., Ui, Storage, Parser, Todo, Deadline, Event, etc
* Javadoc comments on at least half of public classes and public methods.
* Reasonable code quality: follows Java and Git conventions, has no unnecessary commented-out code, and uses small, focused methods without deep nesting.
* Some errors handled using exceptions.
* Good JUnit tests for at least two methods.

## Project management

* Submit some deliverables in at least four of the five iP weeks, from Week 2 to Week 6.
* Follow course requirements such as using Git/GitHub for each increment and completing peer reviews in at least four weeks.
* The final five iP commits must follow the course's required Git commit-message subject convention. Do not rewrite past commits merely to meet this requirement; make further legitimate, small commits when appropriate.

## Documentation

* The product website and user guide provide enough guidance for every non-trivial feature.
* The published documentation has no major formatting errors.
* Final grading is mainly a manual review; automated checks are progress indicators, not the only standard.

# Project-specific requirements

## Java version:

Ensure that Java 25 is used when running the application or build tasks. On macOS, use `sdk use java 25.0.3.fx-zulu` to switch to Java 25 if needed.

## Git

Use lightweight tags unless the user requests an annotated tag.
When proposing or creating a commit message, include enough detail to explain the rationale for the change.
Do not commit or push unless explicitly asked.

# iP grading criteria

When helping with this individual project, keep the following grading requirements in mind. Mention relevant risks or improvements when reviewing code, planning work, or suggesting changes, but do not let this rubric override the user's explicit request.

## Implementation

For full marks, the final submission should satisfy all relevant requirements:

* More than 90% of required deliverables completed. Requirements labelled optional or if-applicable do not count in this percentage.
* A fit-for-purpose GUI at least as complete as Part 4 of the JavaFX tutorial.
* At least two optional increments completed with AI assistance, as required by the course.
* No major bugs.
* Reasonable object-oriented design, including some inheritance and sensible division of responsibilities into classes.
* Javadoc comments on at least half of public classes and public methods.
* Reasonable code quality: follows Java and Git conventions, has no unnecessary commented-out code, and uses small, focused methods without deep nesting.
* Some errors handled using exceptions.
* Good JUnit tests for at least two methods.

## Project management

* Submit some deliverables in at least four of the five iP weeks, from Week 2 to Week 6.
* Follow course requirements such as using Git/GitHub for each increment and completing peer reviews in at least four weeks.
* The final five iP commits must follow the course's required Git commit-message subject convention. Do not rewrite past commits merely to meet this requirement; make further legitimate, small commits when appropriate.

## Documentation

* The product website and user guide provide enough guidance for every non-trivial feature.
* The published documentation has no major formatting errors.
* Final grading is mainly a manual review; automated checks are progress indicators, not the only standard.