# Packaging Java App Example
The project was built with the goal to understand how a Java application can be compiled into a distribution kit for its further distribution to users who 
do not have a JRE on their PC. The assembly of the application into a distribution kit is done using jlink and jpackage. For correct assembly, you must have 
<b>net framework 3.5</b> or higher, as well as installed <b>WiX toolset</b>. The distribution kit can only be run on a computer with a Windows operating system.

<h2>How to run the project</h2>

<ul>
  <li>To run the application:
    <ul><li><code>mvn clean javafx:runc</code></li></ul>
  </li>
  <p>
  <li>To build a distribution:
    <ul>
      <li><code>mvn clean compile javafx:jlink jpackage:jpackage</code></li>
    </ul>
  </li>
</ul>
<p>
The distribution kit after the end of the build will be located in the directory: \target\dist
