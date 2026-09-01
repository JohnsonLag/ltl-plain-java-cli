## `ltl` database setup guide.

Complete these steps after installing MySQL and creating the root/administrative account. This guide will allow you to create the `ltl` database and a user specifically for it.

### One-time setup.

Open the `create-ltl-user.sql` file.

Go to this line:

``` sql
ALTER USER ltl_user IDENTIFIED BY 'REPLACE_THIS_WITH_A_PASSWORD_THAT_YOU_CREATE';
```

Replace the `REPLACE_THIS_WITH_A_PASSWORD_THAT_YOU_CREATE` text with the password you want to assign to the user. In addition, place that password in your `.env` file.

Log in to MySQL.

In the interactive prompt, run each of the following statements, in this order:

```
.\ create-ltl-db.sql
.\ create-entries-table.sql
.\ create-ltl-user.sql
```

After running, change the password in the `create-ltl-user.sql` file back to `REPLACE_THIS_WITH_A_PASSWORD_THAT_YOU_CREATE`. The password should stay in the `.env` file, and the `.env` file should not be committed to version control.

### Regular use.

To log into the `ltl` database on the command line, you can run:

```
mysql -D ltl -u ltl_user -p
```

Then enter your password when prompted.
