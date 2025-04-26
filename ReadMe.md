Digital Wallet App
---
Problem
---
A wallet is used to store money digitally and can be used to pay for
services. Your task is to build a wallet that has the following
abilities:
1. Register a user
2. Top up money to a user wallet
3. Check balance for a user
4. Transfer money between two users
5. View history of all transactions (global and for a user)
All transactions are to be assumed to be within the ecosystem of the
wallet i.e. you cannot send money to a person who is not registered on
the same wallet system. All transactions should be recorded and be
auditable.
---
Expectation
---
These abilities must be exposed via a command line interface.
For example, you could make a menu-driven solution that reads like:
1. Register a user
2. Top up money into your digital wallet
3. Transfer Money to another wallet on the same system
4. Check balance
5. Check all transactions in the system
6. Check all transactions that a user has been a part of
Please choose an option:
The menu design (and the structure of the program) is up to you. You
don’t need to persist the data; the assumption is that when the
program restarts, the data will be lost.
---