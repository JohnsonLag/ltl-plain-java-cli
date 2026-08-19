## One-time setup.

Edit your own password into the create-ltl-user.sql file.

Log in to MySQL.

In the interactive prompt, run each of the following statements, in this order:

.\ create-ltl-db.sql
.\ create-entries-table.sql
.\ create-ltl-user.sql

## Regular use.

To log into the `ltl` database on the command line, you can run:

mysql -D ltl -u ltl_user -p
