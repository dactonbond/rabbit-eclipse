<font color='red'>The development of this project has been stopped.</font>

<br /><br />
**Version 1.2.1 is available now**

Eclipse update site: http://rabbit-eclipse.googlecode.com/svn/site/updatesite
<br />Or try https if the above doesn't work for you: https://rabbit-eclipse.googlecode.com/svn/site/updatesite

<br /><br />
## What is Rabbit? ##
Rabbit is a statistics tracking plug-in for Eclipse. It runs silently in the background to record how you spend your time within Eclipse and reports the data back to you whenever you want it to - in a useful way. Currently, it can tell you the following:

  * **Commands** - How often you use each commands (cut, copy, paste etc), do you know which is your favorite?
  * **Editors and Views** - Time spent using different tool within Eclipse, such as Java Editor, Outline view.
  * **Perspectives** - Time spent using different perspectives.
  * **Sessions** - Time spent using Eclipse.
  * **Resources** - Time spent working on difference resources such as files, projects.
  * **Java Elements** - Time spent working on Java elements such as classes, methods.
  * **Launches** - Launches such application runs, debug runs etc, and the relevant files will be recorded too when you step into them using the during debugging.
  * **Task** - Time spent working on tasks (tasks in Task List view) and resources.


Rabbit is intelligent, it only tracks time when Eclipse is active, that means if Eclipse's window is not focused, tracking will be paused, and if Eclipse's window is focused but no keyboard/mouse event is received in a certain amount of time, tracking will also be paused. So leave Eclipse open and go out for launch if you like, Rabbit will be sleeping while you eat!

The Rabbit view is where you can view the data in a graphical way, it also has a few useful features to help you see the data the way you want to, like grouping by dates, highlighting different types of elements with colors, and more.

<br /><br />
## Why use Rabbit? ##
It's a fun thing to know what you've done, and it can reveal interesting things about you that you never knew.


<br /><br />
## How to get it? ##
To install Rabbit, you'll need

  * Eclipse 3.4/3.5/3.6 (Other products based on Eclipse 3.4/3.5/3.6 also works, I think)
  * Java SE 6


Then follow the steps:

  * **Step 1. Remove Rabbit 1.0 (If you don't have version 1.0 installed, go to Step 2)**
> > If you installed version1.0 manually, you need to remove it before installing version 1.1/1.2 using the update site. Remove rabbit.core.1.0.0\_xxx.jar and rabbit.ui.1.0.0\_xxx.jar files from Eclipse, it's important that you do this. Then download the converter from the [Downloads](http://code.google.com/p/rabbit-eclipse/downloads/list) page and run it to convert the existing data files to the 1.1/1.2 format, if you wish to keep existing data, otherwise some data will be lost.
  * **Step 2. Install Rabbit version 1.2 using the update site.**
> > Use this update site URL for installation: http://rabbit-eclipse.googlecode.com/svn/site/updatesite<br />**Eclipse 3.4**: Click the menu "Help" -> "Software Updates..." -> "Add Site..." then paste in the update site URL<br />**Eclipse 3.5/3.6**: Click the menu "Help" -> "Install New Software..." -> uncheck "Group items by category" if needed -> "Add..." then paste in the update site URL.



That's it! Whenever you want to view the data, you can open the Rabbit view by going to the menu "Window" -> "Show View" -> "Other" ->  "Rabbit" -> "Rabbit".

And if you want an extra feature, found a bug, or would like to contribute, please go to the [Issues](https://code.google.com/p/rabbit-eclipse/issues/list) page and report it, thanks so much for that! ;-)

Enjoy!


Here are some screenshots for this release:

http://rabbit-eclipse.googlecode.com/files/Rabbit%201.2%20win.PNG

![http://rabbit-eclipse.googlecode.com/files/Rabbit%201.2%20linux.png](http://rabbit-eclipse.googlecode.com/files/Rabbit%201.2%20linux.png)

![http://rabbit-eclipse.googlecode.com/files/Rabbit%201.2%20mac.png](http://rabbit-eclipse.googlecode.com/files/Rabbit%201.2%20mac.png)

