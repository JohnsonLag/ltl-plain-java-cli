@ECHO OFF

SET mvn_path="C:\path\to\mvn"

REM The below line installs additional, necessary dependencies and compiles the ltl code.
%mvn_path% clean compile