@echo off
REM ============================================================
REM build.bat — Compile and Deploy the College Seminar Portal
REM
REM What this script does:
REM  1. Compiles all Java source files in src/
REM  2. Copies compiled .class files to WebContent/WEB-INF/classes/
REM  3. Copies db.properties to WEB-INF/classes/ (for classpath access)
REM  4. Copies the entire WebContent/ folder to Tomcat's webapps/
REM
REM Run this from the project root:
REM     build.bat
REM
REM IMPORTANT: Edit the paths below if your installation differs.
REM ============================================================

REM ---- Configuration ----
set PROJECT_ROOT=%~dp0
set TOMCAT_HOME=C:\Users\DELL\Downloads\apache-tomcat-9.0.122-windows-x64\apache-tomcat-9.0.122
set JAVA_HOME=C:\Program Files\Java\jdk-24
set APP_NAME=SeminarPortal

REM Paths derived from configuration
set SERVLET_API=%TOMCAT_HOME%\lib\servlet-api.jar
set JSP_API=%TOMCAT_HOME%\lib\jsp-api.jar
set MYSQL_JAR=%PROJECT_ROOT%WebContent\WEB-INF\lib\mysql-connector-j-26.7.0.jar
set SRC=%PROJECT_ROOT%src
set CLASSES_DIR=%PROJECT_ROOT%WebContent\WEB-INF\classes
set DEPLOY_DIR=%TOMCAT_HOME%\webapps\%APP_NAME%

echo.
echo ============================================================
echo  College Seminar Portal — Build Script
echo ============================================================
echo.

REM ---- Step 1: Create classes directory ----
if not exist "%CLASSES_DIR%" mkdir "%CLASSES_DIR%"
echo [1/4] Created classes directory: %CLASSES_DIR%

REM ---- Step 2: Compile Java source files ----
echo [2/4] Compiling Java source files...
echo.

"%JAVA_HOME%\bin\javac" ^
    -encoding UTF-8 ^
    --release 11 ^
    -cp "%SERVLET_API%;%JSP_API%;%MYSQL_JAR%" ^
    -d "%CLASSES_DIR%" ^
    "%SRC%\util\DBUtil.java" ^
    "%SRC%\model\Event.java" ^
    "%SRC%\model\Student.java" ^
    "%SRC%\model\Registration.java" ^
    "%SRC%\dao\AdminDAO.java" ^
    "%SRC%\dao\EventDAO.java" ^
    "%SRC%\dao\StudentDAO.java" ^
    "%SRC%\dao\RegistrationDAO.java" ^
    "%SRC%\controller\EventListServlet.java" ^
    "%SRC%\controller\RegisterEventServlet.java" ^
    "%SRC%\controller\MyRegistrationsServlet.java" ^
    "%SRC%\controller\AdminLoginServlet.java" ^
    "%SRC%\controller\AdminDashboardServlet.java" ^
    "%SRC%\controller\EventManagementServlet.java" ^
    "%SRC%\controller\ViewRegistrationsServlet.java" ^
    "%SRC%\controller\AdminSetupServlet.java"

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo [ERROR] Compilation failed. Fix the errors above and run build.bat again.
    pause
    exit /b 1
)

echo.
echo [2/4] Compilation successful!

REM ---- Step 3: Copy db.properties to classpath ----
echo [3/4] Copying db.properties to WEB-INF/classes/...
copy /Y "%PROJECT_ROOT%db.properties" "%CLASSES_DIR%\db.properties" > nul
echo [3/4] db.properties copied.

REM ---- Step 4: Deploy to Tomcat ----
echo [4/4] Deploying to Tomcat webapps/%APP_NAME%/...

if exist "%DEPLOY_DIR%" (
    rmdir /S /Q "%DEPLOY_DIR%"
)
xcopy /E /I /Q "%PROJECT_ROOT%WebContent" "%DEPLOY_DIR%"

echo [4/4] Deployment complete.
echo.
echo ============================================================
echo  BUILD SUCCESSFUL
echo  Application deployed to: %DEPLOY_DIR%
echo.
echo  Make sure Tomcat is running, then open:
echo  http://localhost:8080/%APP_NAME%/
echo ============================================================
echo.
pause
