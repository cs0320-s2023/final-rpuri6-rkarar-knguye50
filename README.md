# Capstone Sprint 5: Maps Integration

- Completed by: Kevin Nguyen and Patrick Ortiz
- Time spent: 22 hours
- Link to Github Repository: https://github.com/cs0320-s2023/REALsprint-5-knguye50-portiz3
  - Note: our repository needed to be changed, so this link is
    the actual link to our repository, not the original one
    provided to us.

Collaboration - khu21, klee161

# Design Choices

For user story 2, the call to show all of the redlining data is http://localhost:3232/GeoSearch?minLat=-90&minLong=-180&maxLat=90&maxLong=180, which uses the
bounds logic from user story 3. the bound given covers the globe, which is equivilant to showing the user the full redlining data.

The geoJSON folder in backend/src/csvapi/geoJSON handles the reading, and recreation of GeoJSON files, GeoSearch is the handler being called, and JsonFilter is a class that handles logic regarding determining if a feature is within the specified bounds.

# To Run User Story 3

Start the backend server first in backend\src\main\java\server\Server.java
The API Call takes the form of http://localhost:3232/GeoSearch?minLat=(minimum latitude)&minLong=(minimum longitude)&maxLat=(maximum latitude)&maxLong=(maximum longitude), where every variable is a double.

# To Run Tests

For backend testing, call mvn test in backend/src/test.
TODO: (Include frontend tests here)

### Reflection

Our finished Maps product, as a fullstack project, involved many systems that were no produced by us at any any level, including:

- on the back-end:
  - IDE: IntelliJ
  - Coding language: Java
  - Package manager: Maven
  - Testing Framework: JUnit
  - Server: Spark
- on the front-end:
  - IDE: VS Code
  - Coding Languages: Typescript, HTML/CSS
  - Package manager: Node Package Manager (npm)
  - UI and Webpage: React
  - Maps: react-map-gl (React wrapper), Mapbox GL JS
  - Testing: Jest
    As seen from this list, there were many contributors to our project that made it possile. When running our capstone demo frontend, we rely on the labor of those who have upkept React and the react-map-gl React wrapper for Mapbox GL JS to make sure our webpage loads as intended and displays a detailed map. On top of this, the preparations for this demo, such as code writing, packaging, and debugging were possible thanks to the teams working on VS Code, npm, and Jest, respectively. This is only the frontend. On the backend, much the same can be said of the importance of Spark and its ability to let us run our server, and of IntelliJ, Maven, and JUnit, for allowing us to edit code, package code, and test it, respectively.

It is clear to see that software engineering is an interdependent field that relies on the contribution of many teams who write, debug, test, and deploy important tools as a career and toward helping people complete their projects and accomplish more.

The question of whose labor went into our project in the end, in our opinion, is still largely from us as students. Thinking through our implementation and doing the labor of bring our project to completion is not subsumed in the labor of those who developed the tool we made. Ultimately, their contributions are separate but highly beneficial to our work.
