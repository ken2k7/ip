# Kenbot text-UI test plan

This file is the single record of the text-UI ("black box") test cases for
Kenbot. Each test case starts a **fresh** run of the program, types the listed
input lines into the console, and compares everything the program printed
against the expected output.

Run the whole plan with:

```bash
python3 .claude/skills/test-ui/scripts/run_ui_tests.py
```

## How this file is read

* Every `## ` heading whose section contains an `**Input:**` block is a test
  case. The first word of the heading (for example `TC3`) is its identifier.
* `**Aim:**` says what the test case is checking, so a reader knows why it
  exists.
* `**Input:**` lists the lines typed into the console, one per line. Every test
  case must end with `bye`, because that is the only command that ends the
  program cleanly.
* `**Expected output:**` lists what the program should print *after* the
  greeting. The greeting is recorded once in the `Greeting` section below and
  is added automatically, so it does not have to be repeated.
* Trailing spaces at the end of a line and blank lines at the very end of the
  output are ignored when comparing, since a reader cannot see them.

The first test case that fails ends the session immediately; the expected
output, the actual output, and the difference between them are reported, and
the remaining test cases are not run.

## Greeting

The banner and welcome message printed at the start of every run. It is
prepended to each test case's expected output.

```text
____________________________________________________________
 _  __          _           _
| |/ /___ _ __ | |__   ___ | |_
| ' // _ \ '_ \| '_ \ / _ \| __|
| . \  __/ | | | |_) | (_) | |_
|_|\_\___|_| |_|_.__/ \___/ \__|

                 Kenbot
Yo! I'm Kenbot
How may I help you today?
____________________________________________________________

```

## TC1 - Greet and exit

**Aim:** Check that the program greets the user on startup and says goodbye
when given the `bye` command, without any task in the list.

**Input:**

```text
bye
```

**Expected output:**

```text
____________________________________________________________
Peace! See you soon!
____________________________________________________________
```

## TC2 - Add a to-do

**Aim:** Check that `todo` creates a `Todo`, which is displayed with the `[T]`
type icon and an empty status icon, and that the running task count is shown.

**Input:**

```text
todo read book
bye
```

**Expected output:**

```text
____________________________________________________________
Got it. I've added this task:
  [T][ ] read book
Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
Peace! See you soon!
____________________________________________________________
```

## TC3 - Add a deadline

**Aim:** Check that `deadline ... /by ...` is split correctly and that the
deadline is displayed after the description as `(by: ...)`.

**Input:**

```text
deadline return book /by Sunday
bye
```

**Expected output:**

```text
____________________________________________________________
Got it. I've added this task:
  [D][ ] return book (by: Sunday)
Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
Peace! See you soon!
____________________________________________________________
```

## TC4 - Add an event

**Aim:** Check that `event ... /from ... /to ...` is split into three parts and
that the start and end times are displayed in the right order.

**Input:**

```text
event project meeting /from Mon 2pm /to 4pm
bye
```

**Expected output:**

```text
____________________________________________________________
Got it. I've added this task:
  [E][ ] project meeting (from: Mon 2pm to: 4pm)
Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
Peace! See you soon!
____________________________________________________________
```

## TC5 - List tasks of every type

**Aim:** Check that tasks of all three types can be held at once, that the
task count increases with each one, and that `list` numbers them from 1 in the
order they were added.

**Input:**

```text
todo read book
deadline return book /by Sunday
event project meeting /from Mon 2pm /to 4pm
list
bye
```

**Expected output:**

```text
____________________________________________________________
Got it. I've added this task:
  [T][ ] read book
Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [D][ ] return book (by: Sunday)
Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [E][ ] project meeting (from: Mon 2pm to: 4pm)
Now you have 3 tasks in the list.
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[T][ ] read book
2.[D][ ] return book (by: Sunday)
3.[E][ ] project meeting (from: Mon 2pm to: 4pm)
____________________________________________________________
____________________________________________________________
Peace! See you soon!
____________________________________________________________
```

## TC6 - Mark and unmark a task

**Aim:** Check that `mark` sets the status icon to `X` and `unmark` clears it
again, and that the change is still visible in a later `list`.

**Input:**

```text
todo read book
mark 1
list
unmark 1
list
bye
```

**Expected output:**

```text
____________________________________________________________
Got it. I've added this task:
  [T][ ] read book
Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
Nice! I've marked this task as done:
  [T][X] read book
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[T][X] read book
____________________________________________________________
____________________________________________________________
OK, I've marked this task as not done yet:
  [T][ ] read book
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[T][ ] read book
____________________________________________________________
____________________________________________________________
Peace! See you soon!
____________________________________________________________
```

## TC7 - Reject a deadline with no /by part

**Aim:** Check that a malformed `deadline` command is rejected with a usage
message instead of creating a task.

**Input:**

```text
deadline return book
bye
```

**Expected output:**

```text
____________________________________________________________
Use: deadline DESCRIPTION /by DATE_OR_TIME
____________________________________________________________
____________________________________________________________
Peace! See you soon!
____________________________________________________________
```

## Not yet covered

These behaviours are deliberately absent from the plan because the program
currently crashes instead of producing output that could be expected. Add a
test case for each once the command loop handles the error:

* `mark` with a missing, non-numeric, or out-of-range task number.
* An unrecognised command, which is currently stored as a plain task rather
  than reported as an error.
* Reaching the end of the input without typing `bye`.
