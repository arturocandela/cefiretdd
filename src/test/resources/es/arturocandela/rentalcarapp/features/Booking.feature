Feature: Booking a Car with Promotions
  Como usuario
  Quiero que exista promociones
  Para que sean aplicadas en el momento de la reserva.

  Reglas:
   - Sólo una promoción debe estar activa en un mismo momento
   - Sólo se aplican a las reservas de coches con combustible Gasolina
   - Puede haber 0 promociones activas


  Scenario: An active promotion will be applied when a user books a gasoline car
    Given there is an active promotion
    And  there is an available gasoline car
    When the user books a "gasoline" car
    Then a Promotion will be applied to the Booking

  Scenario: An active promotion wont be applied when a user books a diesel car
    Given there is an active promotion
    And  there is an available diesel car
    When the user books a "diesel" car
    Then the Booking will not get a promotion applied