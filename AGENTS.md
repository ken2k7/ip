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

# Testing workflow after code changes

Kenbot is a console program, so every change to its behavior must be checked
against the text-UI test plan before the change is reported as done. Follow
these two steps after **each** code update to `src/main/java`, without waiting
to be asked.

## 1. Update the test plan if needed

`test/ui-test-plan.md` is the single record of the text-UI test cases. Update it
whenever a code change affects what the program prints or accepts:

* **New or changed command** - add a test case, or update the affected one.
* **Changed wording, spacing, or formatting of any output** - update the
  expected output of every test case that shows it, and update the `Greeting`
  section if the banner or welcome message changed.
* **Newly handled error** - add a test case for it, and remove the matching
  entry from the plan's "Not yet covered" section.

Write the expected output from what the program *should* print. Never paste in
what it currently prints to make a failing test pass: that turns a bug into a
rule. If a test fails because the intended behavior genuinely changed, update
the expected output and say so explicitly.

No test plan update is needed for a change that cannot alter console output,
such as a pure refactoring, a Javadoc-only edit, or a rename of a private field.
Say which of these applies rather than silently skipping the step.

## 2. Run the text-UI tests

Invoke the `test-ui` skill. It is defined in `.claude/skills/test-ui/`, and any
agent can run it directly from the repository root:

```bash
python3 .claude/skills/test-ui/scripts/run_ui_tests.py
```

Use Java 25 (see "Java version" below). The runner compiles the sources itself,
so no separate build step is needed.

Report the outcome honestly:

* **All cases passed** - say so, and include the console session.
* **A case failed** - the runner stops at the first failure and prints the
  expected output, the actual output, and the difference. Show that report, fix
  the cause in `src/main/java`, and run the plan again. Do not describe the
  change as working until the full plan passes.
* **Not run** - if the tests could not be run at all, say that plainly instead
  of implying the change was verified.

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