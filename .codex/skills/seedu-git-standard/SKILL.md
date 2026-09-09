---
name: seedu-git-standard
description: The SE-EDU Git conventions for commit messages and branch names that this project must follow. Use when writing or proposing a commit message, when creating a branch, and when checking whether existing commits follow the required convention.
---

# SE-EDU Git conventions

Source: https://se-education.org/guides/conventions/git.html

Every commit made in this project follows these rules. The course grades the
final five iP commits against the subject-line convention, so it matters.

## Subject line

* **Imperative mood.** `Add README.md`, not `Added README.md` or
  `Adding README.md`. The test: the subject should complete the sentence
  "If applied, this commit will ___".
* **Capitalise the first letter.** `Move index.html file to root`, not
  `move index.html file to root`.
* **No full stop at the end.** `Update sample data`, not `Update sample data.`
* **Aim for 50 characters, 72 is the hard limit.**
* An optional scope prefix is allowed: `Person class: Remove static imports`.

## Body

Only when the subject alone is not enough.

* Separate the subject from the body with one blank line.
* Wrap the body at 72 characters.
* Separate paragraphs with blank lines, and use bullet points where they read
  more clearly than prose.
* **Explain what and why, not how** — the diff already shows how.
* Do not repeat what the code comments already say.
* A useful order: state the current situation, why it needs to change, what
  this commit does, and why that way.
* Avoid "currently" and "originally" when describing the existing state.

## Branch names

* Meaningful keywords in **kebab-case**: `refactor-ui-tests`.
* For a branch addressing an issue, lead with the number:
  `1234-ui-freeze-error`.
* Where the course prescribes a branch name for an increment, such as
  `branch-Level-8`, the course's instruction wins.

## Project preference: keep subjects very short

In this repository the author prefers a single-line subject of **at most four
words, with no body**, unless a body is genuinely needed. This is stricter than
the standard and stays compliant with it, so follow it by default:

```
Add handoff notes
Write the user guide
Merge branch-A-CodingStandard
```

## Checking existing commits

```bash
git log --pretty=format:'%s' -20
```

Read each subject for: imperative verb, leading capital, no trailing period,
and length under 72. Never rewrite history that has already been pushed just to
satisfy this; make further small, legitimate commits instead.
