Description
===========

This application is intended to be used as an aid during the GOSTA work fair
to help the students navigate the exhibition and prepare for the meetings with
the exhibitors.

User Manual
===========

There are two menus for navigating through the application. One bottom
navigation menu for the most commonly used pages; the list of exhibitors, the
map of the exhibiton hall, the schedule and the extended menu. In the extended
menu there are some additional menu options such as navigation to the sponsors
page and the QR-reader.

Upon opening the application the list of exhibitors is displayed, click on a
company's name to display more information about them. When the list of
companies is displayed shaking the phone will produce a popup window with a
randomly selected company.

To reach the map of the exhibition hall click the location pin icon in the
bottom navigation menu. To zoom in and out make pinch gestures on the screen
and drag the image around using one finger. Click on a showcase to see which
company is located there.

The calendar icon in the bottom navigation menu represents the page holding the
schedule for the fair. A list of events is displayed and clicking an event
initiates a popup window with more information about the event.

Clicking the forth and last icon of the bottom navigation menu displays the
menu page, which can also be used to reach the previously mentioned pages. And
additional pages such as; a page displaying information about which companies
that are sponsoring the fair and a QR-reader for scanning QR-codes.

Developer Manual
================

Client
------

Packages
--------
The android client packages are divided by category. Java classes for each activity can be found in the package se.gosta.activity. Java classes handling network and containing all network related code is located in the package se.gosta.net. The package se.gosta.storage contains all classes creating the main objects, for instance the company and sponsor objects. Finally, the package se.gosta.utils contains all helper classes needed for the client.

Classes
-------
No activity in the se.gosta.activity package will be explained here. See comments inside classes for more info regarding the particular class.

FairFetcher.java is used to fetch data from Json-files on the server. The activities register listeners to be notified when data is fetched.

Company.java represents a company at the fair and contains all information regarding the company. This information is fetched from Json through FairFetcher.

The Java class Event represents an event at the fair, for example at what time the fair opens. MenuOption.java is used to create Menu-objects for displaying a menu in the MenuActivity.

Sponsor.java represents a sponsor of the fair. This class contains all information regarding the actual sponsor, including their logotype.

Session.java is a helper class for the client to keep track on the current company. This is for example necessary to display information about the correct company when the user clicks on a company.

Utils.java is a helper class for handling logotypes for each company and storing them on the internal memory.

XML-files
---------
The XML-files are divided into a number of folders based on category. The folders anim and animator contains all XML-files that controls animations. The folder color contains an XML- file creating a tint for the bottom navigation bar depending on the selected item.

The folder drawable contains all drawables and images used in the application that are not fetched from the server and xml-files for rounded corner and gradient backgrounds.

The layout folder contains all XML-files controlling the layout of each activity. These layouts are named accordingly. It also contains the layouts of the popups, menus and a custom listview.

The menu folder contains the XML-files for the bottom navigation bar and the popup menu. Mipmap is used for the launcher icons and if a customized icon is desired.

The values folder contains the XML-files regarding colors, strings and styles. Change values here instead of hard coding it.
Finally, the xml folder contains XML-files for network configuration and miscellaneous.

Servlet
-------
The servlet is powered by winstone and is divided in a number of sections. These sections are schedule, cases, sponsors and companies. The servlet xml is located in the package server/WEB-INF/web.xml.

The database is called companies.db and is located in the folder /server. This is the database used to create Json-files on the server. The database contains the tables companies, sponsors, cases and schedule. The corresponding Java class for parsing the data to Json is located in the folder server/WEB-INF/classes.

To start the server, there is a script located in the server folder called start_server.sh. Run this script inside the folder to start up the server. The script checks which OS the user is running, and then compiles and runs the Java classes in server/WEB-INF/classes.

Thanks to
=========
[ClickableAreasImage](https://github.com/Lukle/ClickableAreasImages/), for the library which the clickable map is using. 

[ZXing](https://github.com/zxing/zxing), for the library used for the QR-reader.

License
=======

Copyright 2019 Annie Petersson, Johannes Carli

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0
Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
