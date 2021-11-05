# Tidal recruiting refactoring test

###### Please do not share this test or your answers on github or any other place where it can be found by a third party. 

##Intro
The code in this project is a isolated and very simplified version of our add-track-to-playlist business code.

###Setup notes
- We have used Guice for dependency injection (in the real service we currently use EJB injection), for the setup to be simpler.
- We have added a fake PlaylistDaoBean that only returns a hardcoded playlist for simplicity.

##Assignments
1. Add necessary unit tests to the existing code to both see that the code works as expected, and to avoid breaking the code when refactoring
2. Refactor the existing code until you are satisfied with it
3. Add a delete track method with appropriate unit tests

##Feel you are done or have questions?
When you have managed to make yourself believe that you are done, please send us a zip file with the code. If you have any question about the test do not hesitate to contact us.

###One last thing..
Just as important as the refactoring and testing itself - please come with comments on the test project, the setup of it or other things you think can improve or change. Also documentation on what you have done, and why is going to make the evaluation easier for us, and _may_ count as a plus. It's up to you if you decide to document in a separate file, or in the code itself. 

Enjoy!
