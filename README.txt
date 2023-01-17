________________________________README_______________________________
Denne readme bygger på og antager at du allerede har intellij IDE, og git (deri) installeret og en
 grundlæggende forståelse for hvordan det bruges. En anden IDE (eksempelvis Eclipse) kan også bruges.
Guiden antager også at du bruger windows, men processen er meget det samme på andre styresystemer.
Det antages desuden at brugeren allerede har Java installeret.
Besøg ellers: https://www.java.com/download/ie_manual.jsp


______________________________STYRESYSTEM______________________________
Spillet er kun testet på Windows 10 og 11.
Spillet kræver nyeste version af Java (er ikke testet på ældre versioner)
Spillet kræver Java JDK 19.x eller lignende (er ikke testet på ældre versioner)

Gem & Load funktioner fungerer sandsynligvis ikke på unix-baserede operativsystemer (Navngiveligt MacOS & Linux)


_________________________ GUIDE TIL AT BYGGE JAR_________________________
Åbn repoet i IntelliJ.
Tryk file -> Project structure -> artifacts
Tryk på “+” -> JAR _> From modules with dependencies
Vælg module Spil_Matador
Vælg Main Class:  dtu.matador.game.GameController
Gem ændringer og luk vinduet.
Vælg i toppen af skærmen Build -> build artifacts-> Spil_Matador.jar -> build
Hvis du kigger i out/artifacts vil .jar filen nu ligge der, og du kan flytte den til en anden mappe hvis det ønskes.
Herefter kan den køres ved
at højreklikke på files -> run with -> run with Java



___________________________ GUIDE TIL AT BYGGE_________________________
Tryk på file i toppen af skærmen i IntelliJ og naviger:
file -> new -> project from version control
indsæt linket https://github.com/luucmeldgaard/Spil_Matador_Januar
tryk clone
Lokaliser pom.xml
højreklik -> maven -> reload project

Lokaliser filen GameController.class
kør

eller

Tryk på Build->Build



____________________________ GUIDE TIL TEST __________________________


JUNIT TESTS
Åbn repoet i IntelliJ.
Øverst til højre vælges den main method der ønskes at køre. Vælg en fra den ønskede JUNIT test.
Tryk på den grønne kør-knap.

ACCEPTANCE TESTS
Acceptance testing kan køres enten fra en bygget jar, eller direkte gennem IntelliJ.

Spillet gennemspilles generelt, indtil man ser at acceptance testen er udfyldt. Nogle acceptance
tests kan tage tid at gennemgå.
Man kan i disse tilfælde i stedet for at spille “normalt” trykke:
andet -> snydekoder

Herfra er en række muligheder for at ændre ting i spillet.
