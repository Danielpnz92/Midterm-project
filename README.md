Modelo
----------------
1. Cuentas
BasicAccount: contiene las variables comunes a todos los tipos de cuenta:
- Id: id de cuenta
- balance: dinero en la cuenta
- account_holder_id: id de propietario principal de cuenta
- secondary_owner_id: id del propietario secundario si existe. Al instanciar una cuenta este usuario de da de alta como Optional<>
- penalty_fee: coste de cuenta a 0 al actualizar el balance
- creation_date: fecha de creación de la cuenta

Checking:
los métodos setBalance y setMinimumBalance incluyen lógica adicional de asignación. no se permite asignar un balance mínimo menor de 250.

CreditCard:
en setCreditLimit y setInterestRate se asigna un valor por defecto si el especificado está fuera de un rango. 
en el método getBalance se actualiza el balance en base a su tipo de interés y el tiempo transcurrido desde la última vez que se actualizó el balance (en períodos).

Student:
Se crea una cuenta de Student si el propietario tiene menos de 24 años. Se incluye en la lógica del controlador de Checking

Savings:
sigue la misma lógica para setCreditLimit, setInterestRate y getBalance que en CreditCard (los períodos son años)

AccountBalanceDTO:
Objeto utilizado para modificaciones directas en balance de cuentas, transferencias, etc.

2. Usuarios
User: contiene las variables comunes a todos los tipos de usuario:

Roles y Status: clases enum

Rutas
-------------------
BasicAccountController: como todas las cuentas parten de los datos de BasicAccount, la mayoría de la lógica se ha incorporado en este apartado. 
Quedaría a posteriori separar toda esta lógica en el servicio correspondiente (BasicAccountService) e implementar más modularidad en el código:
validaciones de id de usuario, de cuenta propia, de balance suficiente, de credenciales correctas para envío, de autorización...

GETS------>
- /api/accounts/all: devuelve todas las cuentas creadas con su información básica
- /api/accounts/balance/{id}: devuelve el balance de una cuenta concreta, se especifica en el enunciado que debe ser accesible solo para admin, pero
se ha extendido a los demás tipos de usuario para poder probarse en los test unitarios con la notación @WithMockUser para simular el logueo de
un tipo de usuario concreto en cada prueba
- /api/own_accounts/balance/{id}: devuelve el balance de una cuenta propia especificada por el usuario, si no es suya devuelve un error 404 
"Account not owned by user", para ello se comprueba que el usuario logueado sea propietario principal o secundario de la cuenta mediante las
queries JPA: findAccountsOfCurrentUser1 para propietarios principales, y findAccountsOfCurrentUser2 para propietarios secundarios de cuentas

POST-------> Para crear cada uno de los tipos de cuenta por separado en su correspondiente controlador:
- /api/accounts/checking
- /api/accounts/credit_card
- /api/accounts/savings
- /api/accounts/student_account
Se ha creado una para crear usuarios con rol THIRD_PARTY para probar
- /api/users/third_party

PATCH------->
- /api/accounts/balance_modify/{id}: modifica el balance de una cuenta especificada. Puede acceder a cualquier cuenta y solo mediante usuario admin
- /api/accounts/transfer/{senderAccountId}/{receiverAccountId}/{receiverName}: Transferencias. Se pueden ejecutar desde un ACCOUNT_HOLDER propietario de
la cuenta que envía el dinero, a otro ACCOUNT_HOLDER propietario de la cuenta que lo recibe. Si alguna de estas condiciones no se cumple saltan 
diferentes excepciones:
    - "Account not owned by user"
    - "The owner of the destination account does not match the name provided, the transfer won't take place"
    - "Not enough funds in sender account"
    Todas estas excepciones han sido probadas y validadas
- /api/accounts/third_party/{amount}/{accountId}/{secretKey}: Permite el envío/recepción de dinero de un usuario con rol THIRD_PARTY a cualquier otra
cuenta Checking, StudentAccount o Savings. Se incluyen comentarios en el interior del método para seguir la lógica aplicada. Puede saltar estas 
excepciones:
    - "Account not found"
    - "Secret key mismatched with that account id"
    
DELETE------->
- /api/accounts/delete/{id}: borrar cuenta. Desde aquí se puede borrar cualquier tipo de cuenta


