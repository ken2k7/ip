# Kenbot User Guide

Kenbot is a console chatbot for keeping track of things you need to do. You type
a command, it answers, and it remembers everything between runs by saving your
tasks to a file.

```
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

## Getting started

1. Make sure you have **Java 25** installed.
2. Download `kenbot.jar`, or build it yourself with `./gradlew shadowJar`.
3. Run it: `java -jar kenbot.jar`
4. Type a command and press Enter. Type `bye` when you are done.

Kenbot keeps your tasks in `data/Kenbot.txt`, created next to wherever you start
it. You never need to make that folder yourself.

## Commands at a glance

| Command | What it does |
| --- | --- |
| `todo DESCRIPTION` | adds a task with no date |
| `deadline DESCRIPTION /by DATE` | adds a task due by a date |
| `event DESCRIPTION /from DATE /to DATE` | adds a task spanning two dates |
| `list` | shows every task, numbered |
| `mark NUMBER` | marks a task done |
| `unmark NUMBER` | marks a task not done |
| `delete NUMBER` | removes a task |
| `find KEYWORD` | shows tasks whose description contains the keyword |
| `bye` | exits |

## How to write dates

Dates go in as `yyyy-mm-dd`, and you may add a time after a space:

```
2019-10-15
2019-10-15 1800
```

Kenbot shows them back in a friendlier form — `Oct 15 2019` and
`Oct 15 2019 1800`.

Anything that is not a real date is refused, so `Sunday` and `15-10-2019` will
both be rejected with a reminder of the accepted format.

## Adding a to-do

A task with nothing but a description.

Example: `todo read book`

```
____________________________________________________________
Got it. I've added this task:
  [T][ ] read book
Now you have 1 tasks in the list.
____________________________________________________________
```

## Adding a deadline

A task that has to be finished by a particular date.

Example: `deadline return book /by 2019-10-15`

```
____________________________________________________________
Got it. I've added this task:
  [D][ ] return book (by: Oct 15 2019)
Now you have 2 tasks in the list.
____________________________________________________________
```

## Adding an event

A task that runs from one date to another. Times are optional on either end.

Example: `event project meeting /from 2019-10-15 1400 /to 2019-10-15 1600`

```
____________________________________________________________
Got it. I've added this task:
  [E][ ] project meeting (from: Oct 15 2019 1400 to: Oct 15 2019 1600)
Now you have 3 tasks in the list.
____________________________________________________________
```

## Listing your tasks

Example: `list`

```
____________________________________________________________
Here are the tasks in your list:
1.[T][ ] read book
2.[D][ ] return book (by: Oct 15 2019)
3.[E][ ] project meeting (from: Oct 15 2019 1400 to: Oct 15 2019 1600)
____________________________________________________________
```

The letter in the first brackets is the kind of task — `T` for to-do, `D` for
deadline, `E` for event. The second brackets hold an `X` once the task is done.

An empty list says so rather than showing nothing:

```
____________________________________________________________
You have no tasks yet.
____________________________________________________________
```

## Marking a task done, or not done

Use the number shown by `list`.

Example: `mark 1`

```
____________________________________________________________
Nice! I've marked this task as done:
  [T][X] read book
____________________________________________________________
```

Example: `unmark 1`

```
____________________________________________________________
OK, I've marked this task as not done yet:
  [T][ ] read book
____________________________________________________________
```

## Deleting a task

Example: `delete 2`

```
____________________________________________________________
Noted. I've removed this task:
  [D][ ] return book (by: Oct 15 2019)
Now you have 2 tasks in the list.
____________________________________________________________
```

The remaining tasks are renumbered straight away, so `list` always runs from 1
with no gaps.

## Finding tasks

Searches the descriptions. Upper and lower case are ignored, and part of a word
counts — `find book` also finds "bookshop".

Example: `find book`

```
____________________________________________________________
Here are the matching tasks in your list:
1.[T][X] read book
2.[D][ ] return book (by: Oct 15 2019)
____________________________________________________________
```

If nothing matches, Kenbot says so:

```
____________________________________________________________
No tasks match 'xyz'.
____________________________________________________________
```

## Leaving

Example: `bye`

```
____________________________________________________________
Peace! See you soon!
____________________________________________________________
```

Your tasks are already saved by this point — every command that changes the list
writes the file immediately, so nothing is lost even if the program is closed
without typing `bye`.

## Things worth knowing

* **Commands are lower case.** `Todo` and `MARK` are not recognised.
* **Descriptions cannot contain `|`.** Kenbot uses that character to separate
  fields in its save file, so a task containing one is refused when you type it.
* **The numbers `find` shows start from 1** and are not the numbers to use with
  `mark` or `delete`. Run `list` first if you need the real number.
* **A damaged save file does not lose everything.** If Kenbot cannot understand
  some lines, it loads the ones it can and tells you how many it skipped.
