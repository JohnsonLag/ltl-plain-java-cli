## Troubleshooting

**Check if the MySQL service is running.**
```
SQLException: Communications link failure

The last packet sent successfully to the server was 0 milliseconds ago. The driver has not received any packets from the server.
```

Check that MySQL is running.

If using MySQL 8.4 on Windows, running it could look like: Task Manager > Services > MySQL84 > Start.

On a Debian or Ubuntu-based Linux system, that could look like running: `systemctl start mysql84`.

**classpath hyphen error.**
```
Error: Could not find or load main class ?Çôclasspath
Caused by: java.lang.ClassNotFoundException: ?Çôclasspath
```

You may need to edit the `ltl-run.bat` file. Where it says, `%java_path% -classpath`, you may need to manually rewrite the hyphen before `classpath` and then save the file.

**.env file location.**

```
Exception in thread "main" java.lang.RuntimeException: java.io.FileNotFoundException: src\main\.env (The system cannot find the file specified)
```

You may need to move or copy your `.env` file to another folder. Here, you would need to move/copy it to into the `src\main` folder.