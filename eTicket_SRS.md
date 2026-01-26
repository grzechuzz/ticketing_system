

## Specyfikacja Wymagań Oprogramowania dla systemu eTicket

### 1. Wstęp

**1.1. Cel**

Niniejszy dokument opisuje wymagania dla systemu biletowego eTicket w wersji 1.0. System został stworzony jako platforma do zarządzania sprzedażą biletów na wydarzenia. 

Dokument jest przeznaczony dla zespołu deweloperskiego, testerów, klientów oraz zarządu i pełni następujące funkcje:
- Stanowi umowę pomiędzy klientem a zespołem deweloperskim
- Jest fundamentem do projektowania struktury i architektury systemu
- Służy jako baza do tworzenia testów i weryfikacji kryteriów akceptacji

**1.2. Wizja, zakres i cele produktu**
- **Wizja**: Naszą wizją jest usunięcie barier technologicznych w organizacji wydarzeń. Chcemy, aby każdy organizator, niezależnie od budżetu miał dostęp do technologii, która łączy intuicyjną obsługę z inteligentną optymalizacją sprzedaży. Z tego powodu stworzyliśmy eTicket - rozwiązanie, które pozwala maksymalizować zyski przy zachowaniu maksymalnej prostoty użytkowania.

- **Zakres produktu**: Nasz system będzie platformą, która umożliwia:
  - Tworzenie, publikowanie i zarządzanie wydarzeniami przez organizatorów
  - Inteligentną walidację rezerwacji - mechanizm, który zapobiega powstawaniu pojedynczych wolnych miejsc
  - Sprzedaż i generowanie biletów w formacie PDF z unikalnym kodem QR
  - Podgląd podstawowych statystyk sprzedaży dla organizatora
  - Rejestrację, logowanie i zarządzanie kontami użytkowników z podziałem na role

- **Poza zakresem**: System nie będzie obsługiwał:
  - Rzeczywistych transakcji finansowych, nie będzie zintegrowany z zewnętrznymi bramkami płatności
  - Dokumentów księgowych, nie będzie generował faktur ani paragonów
  - Możliwości edycji kształtów sal oraz sektorów, ich układ będzie oparty o predefiniowane szablony (np. prostokąty 10x15)
  
- **Cele produktu**

  Główne cele biznesowe:
  
  - Łatwość wdrożenia. System ma być na tyle intuicyjny, aby organizatorzy mogli z niego korzystać bez potrzeby szkoleń czy kontaktu z pomocą techniczną.<br><br>**Kryterium akceptacji**: W teście użyteczności przeprowadzonym przed wdrożeniem produkcyjnym na grupie 10 nowych użytkowników, co najmniej 8 osób (80%) samodzielnie poprawnie skonfiguruje i opublikuje wydarzenie.
    
  - Zwiększenie efektywności sprzedaży miejsc siedzących. System ma minimalizować liczbę niesprzedanych pojedynczych miejsc poprzez inteligentną alokację miejsc.<br><br>**Kryterium akceptacji**: W ciągu pierwszych 6 miesięcy działania systemu łączna liczba niesprzedanych pojedynczych miejsc w sektorach siedzących nie przekroczy 5% całkowitej puli miejsc udostępnionych we wszystkich wydarzeniach.
  
  Główne cele użytkowników:
  
  - Szybkość zakupu. Użytkownik oczekuje uproszczonego procesu zakupu, aby móc nabyć bilet przy minimalnym wysiłku i liczbie kliknięć.<br><br> **Kryterium akceptacji**: W pierwszym miesiącu funkcjonowania systemu średni czas przejścia procesu zakupowego mierzony od momentu zatwierdzenia miejsc na mapie lub wybrania liczby biletów (dla wydarzeń z ogólną pulą biletów) nie przekroczy 90 sekund.

  - Sprawna moderacja. Organizator oczekuje sprawnego procesu weryfikacji wydarzenia, aby uniknąć opóźnień w zaplanowanym harmonogramie sprzedaży.<br><br>**Kryterium akceptacji**: W ciągu pierwszych 12 miesięcy działania systemu 95% wydarzeń zgłoszonych przez organizatorów jest rozpatrywanych (zatwierdzanych lub odrzucanych z komentarzem) w czasie krótszym niż 24 godziny od momentu przesłania.

**1.3. Definicje, Akronimy i Skróty**
  * Luka - pojedyncze wolne miejsce w rzędzie pomiędzy miejscami zajętymi, trudne do sprzedaży
  * Organizator - użytkownik systemu posiadający uprawnienia do tworzenia i zarządzania wydarzeniami
  * Klient - użytkownik przeglądający ofertę i dokonujący zakupu biletów
  * DTO - Data Transfer Object, obiekt służący do przesyłania danych między podsystemami w celu optymalizacji wydajności


**1.4. Przegląd Dokumentu**

  Pozostała część dokumentu została zorganizowana w następujący sposób:
  * **Rozdział 2 (Opis ogólny)** przedstawia szerszy kontekst projektu, charakteryzuje użytkowników systemu oraz definiuje kluczowe ograniczenia projektowe (technologiczne, budżetowe i prawne).
  * **Rozdział 3 (Wymagania Funkcjonalne)** zawiera szczegółowy opis funkcji systemu (zakup biletów, tworzenie wydarzeń) wraz z kryteriami akceptacji i priorytetami.
  * **Rozdział 4 (Atrybuty jakościowe)** definiuje wymagania niefunkcjonalne, takie jak wydajność, dostępność i integralność danych, oraz przedstawia analizę kompromisów architektonicznych podjętych w celu ich spełnienia.
  * **Rozdział 5 (Analiza rynku)** zawiera porównanie systemu eTicket z istniejącymi rozwiązaniami konkurencyjnymi (Eventbrite, Ticketmaster, Cinema City), wskazując na luki rynkowe, które projekt ma za zadanie wypełnić.

### 2. Opis ogólny

**2.1. Główne funkcje produktu**
- **Zarządzanie wydarzeniami**: utworzenie wydarzenia oraz jego publikacja
- **Przegląd wydarzeń**: lista wydarzeń i szczegółowy widok planu miejsc
- **Zakup biletów**: system umożliwia klientowi zakup biletów na różne typy wydarzeń
- **Inteligentna alokacja i walidacja miejsc**: w przypadku zakupu biletów z przypisanym miejscem system weryfikuje wybory użytkownika, stosując politykę minimalizacji pozostawiania pojedynczych wolnych miejsc
- **Generowanie dokumentów**: automatyczne tworzenie dokumentów w formacie PDF np. bilety z kodami QR 
- **Obsługę procesu zamówień**: koszyk, rezerwacja czasowa miejsca, symulacja płatności
  
**2.2. Klasy użytkowników**
- **Klient**: przegląda wydarzenia, kupuje i rezerwuje bilety
- **Organizator**: tworzy i publikuje wydarzenia, konfiguruje pulę biletów i ceny 
- **Administrator**: odpowiada za konfigurację systemu oraz akceptowanie publikowanych wydarzeń

**2.3. Ograniczenia projektowe**

* **2.3.1. Ograniczenia organizacyjne**
  
  **Ograniczenie:**
  Projekt musi zostać zrealizowany w krótkim czasie około 1-2 miesięcy. Nie ma możliwości implementacji wszystkich funkcji    spotykanych w komercyjnych systemach biletowych.
  
  **Źródło:**
  Edukacyjny charakter projektu oraz ograniczona liczba członków zespołu.
  
  **Wpływ na architekturę:**
  * Rezygnacja z bardziej skomplikowanych funkcjonalności
  * Konieczność wyboru sprawdzonych technologii zamiast eksperymentowania z nowymi rozwiązaniami
  * Wyklucza zaawansowane funkcje optymalizacyjne

* **2.3.2. Ograniczenia technologiczne**
 
  **Ograniczenie:**
  System musi być zbudowany z wykorzystaniem następujących technologii: Java + Spring Boot (backend), React (frontend) oraz PostgreSQL (baza danych).
  
  **Źródło:**
  Decyzja zespołu projektowego wynikająca z posiadanych umiejętności.
  
  **Wpływ na architekturę:**
  * Wymusza architekturę klient-serwer z REST API jako warstwą komunikacji
  * Wymusza wykorzystanie mechanizmów ORM
  * Ogranicza możliwość wykorzystania innych frameworków

* **2.3.3. Ograniczenia biznesowe**

  **Ograniczenie:**
  Całkowity budżet na hosting, infrastrukturę i zewnętrzne usługi wynosi 0 PLN miesięcznie.
  
  **Źródło:**
  Projekt akademicki realizowany bez zewnętrznego finansowania.
  
  **Wpływ na architekturę:**
  * Konieczność doboru infrastruktury opartej wyłącznie o darmowe plany usług
  * Ograniczenia w zakresie wydajności

* **2.3.4. Ograniczenie prawne**
  
  **Ograniczenie:**
  System musi być zgodny z Rozporządzeniem o Ochronie Danych Osobowych (RODO), a wszystkie dane osobowe użytkowników (organizatorów i uczestników) muszą być fizycznie przechowywane na serwerach zlokalizowanych w granicach Europejskiego Obszaru Gospodarczego (EOG).
  
  **Źródło:**
  Prawo Unii Europejskiej.
  
  **Wpływ na architekturę:**
  * Drastycznie zawęża wybór dostawców usług chmurowych do tych, którzy posiadają centra danych w EOG
  * Wymusza implementację mechanizmów do obsługi praw użytkowników (prawo do bycia zapomnianym, prawo do eksportu danych), co musi być uwzględnione w projekcie bazy danych i API
  * Narzuca konieczność anonimizacji danych w środowiskach deweloperskich i testowych

**2.4. Założenia projektowe**

* **2.4.1. Założenie akceptacji walidacji miejsc**
  *   **Założenie:** Zakładamy, że klienci zaakceptują mechanizm inteligentnej alokacji i walidacji miejsc, który blokuje możliwość pozostawiania pojedynczego wolnego miejsca (wymuszając lub proponując wybór innych miejsc) i nie zniechęci ich to do finalizacji transakcji.
  *   **Ryzyko:** Klienci mogą odebrać tę blokadę jako błąd systemu lub irytujące ograniczenie ich swobody, co doprowadzi do porzucenia koszyka i spadku sprzedaży.
  *   **Plan walidacji:**
      *   **Co:** Testy użyteczności na prototypie.
      *   **Jak:** Przeprowadzenie krótkich sesji z 10 użytkownikami, którzy otrzymują zadanie wybrania miejsca w sposób generujący "lukę". Obserwacja czy po otrzymaniu komunikatu blokady rozumieją go i korygują wybór czy rezygnują z zakupu.
      *   **Kiedy:** Na etapie projektowania interfejsu.
      *   **Kto:** Zespół projektowy.

* **2.4.2. Założenie dotyczące praw autorskich**
  *   **Założenie:** Zakładamy, że organizatorzy będą publikować wyłącznie materiały graficzne do których posiadają prawa autorskie, co zwalnia system z konieczności implementacji automatycznych mechanizmów weryfikacji własności intelektualnej. 
  *   **Ryzyko:** Właściciele systemu mogą zostać pociągnięci do współodpowiedzialności prawnej.
  *   **Plan walidacji:** 
      *   **Co:** Weryfikacja mechanizmów prawnych.
      *   **Jak:** Sprawdzenie czy proces tworzenia wydarzenia wymusza na organizatorze akceptację regulaminu.
      *   **Kiedy:** Przed wdrożeniem produkcyjnym.
      *   **Kto:** Zespół projektowy.

### 3. Wymagania Funkcjonalne

**WF-01: Zakup biletów z inteligentną walidacją miejsc**

* **Opis:** Funkcjonalność umożliwia wybór miejsc na mapie. System weryfikuje wybór pod kątem powstawania pojedynczych luk, blokując go, gdy w pobliżu (w tym samym lub sąsiednich rzędach) istnieją alternatywne miejsca pozwalające uniknąć luki.
  
* **Historyjka użytkownika:**
  * Jako klient,
  * chcę wybrać konkretne miejsca obok siebie na mapie sali
  * abym miał gwarancję, że będę siedzieć razem ze znajomymi

* **Cel biznesowy:** Zwiększenie zysku organizatora poprzez zmniejszenie liczby niesprzedanych pojedynczych miejsc.
  
* **Warunki wstępne:** Użytkownik widzi mapę sektora.

* **Warunki końcowe:** Miejsca zarezerwowane, brak zbędnych luk w rzędach (chyba, że było to nieuniknione).

* **Kryteria akceptacji:**
  * **WF-APLIK-01-A**: Optymalny wybór miejsc (Scenariusz główny)
    * Opis: Użytkownik wybiera miejsca w sposób, który nie generuje luki.
    * Kryteria akceptacji:
      * Given: Widzę rząd z wolnymi miejscami 7, 8, 9, 10.
      * When: Wybieram miejsca 7 i 8 (obok już zajętych).
      * Then: System akceptuje wybór i przechodzi dalej.
        
  * **WF-APLIK-01-B**: Wybór miejsc tworzących lukę (Scenariusz alternatywny 1)
    * Opis: Użytkownik robi lukę, ale w tym samym rzędzie są inne miejsca, które luki nie robią.
    * Kryteria akceptacji:
      * Given: Wybieram miejsca 8 i 9, zostawiając pustą lukę na miejscu nr 7.
      * And: Algorytm wykrywa, że w tym samym rzędzie dostępna jest para 7 i 8.
      * When: Klikam "Dalej".
      * Then: System blokuje przejście.
      * And: Wyświetla komunikat: "Ten wybór zostawia pojedyncze wolne miejsce. Dostępne są inne miejsca obok siebie." i podpowiada sugerowane miejsca.
        
  * **WF-APLIK-01-C**: Wybór miejsc tworzących lukę (Scenariusz alternatywny 2)
    * Opis: Użytkownik robi lukę, ale w jego rzędzie nie ma alternatywnych miejsc, są jednak w rzędzie wyżej lub niżej.
    * Kryteria akceptacji:
      * Given: Wybieram miejsca z luką w rzędzie 5.
      * And: W rzędzie 5 brakuje innej możliwości wyboru miejsc obok siebie dla tej liczby osób.
      * And: Algorytm wykrywa, że w rzędzie sąsiednim (4 lub 6) dostępne są wolne miejsca obok siebie nietworzące luki.
      * When: Klikam "Dalej".
      * Then: System blokuje przejście.
      * And: Wyświetla komunikat: "Ten wybór zostawia pojedyncze wolne miejsce. Dostępne są inne miejsca obok siebie."
      * And: Podpowiada sugerowane miejsca w innym rzędzie.

  * **WF-APLIK-01-D**: Wybór miejsc tworzących lukę (Scenariusz alternatywny 3)
    * Opis: Użytkownik robi lukę, a system po przeszukaniu rzędu obecnego oraz sąsiednich nie znajduje nic lepszego.
    * Kryteria akceptacji:
      * Given: Wybieram miejsca tworząc lukę.
      * And: System sprawdza rząd obecny, wyższy i niższy i nie znajduje żadnych lepszych miejsc.
      * When: Klikam "Dalej".
      * Then: System zezwala na wybór (mimo luki), uznając, że klient nie ma innej opcji.
      * And: Pozwala na przejście dalej.
        
  * **WF-APLIK-01-E**: Konflikt rezerwacji (Scenariusz wyjątkowy)
      * Opis: Użytkownik wybiera miejsce, ale w tej samej chwili zajmuje inny klient
      * Kryteria akceptacji:
        * Given: Widzę wybrane miejsce jako wolne
        * When: Klikam na to miejsce, jednak serwer przetworzył żądanie drugiego klienta ułamek sekundy wcześniej.
        * Then: System blokuje moją rezerwację.
        * And: Wyświetla komunikat "To miejsce jest niedostępne".

**WF-02: Tworzenie i publikacja wydarzenia**

* **Opis:** Umożliwia organizatorom szybkie i łatwe utworzenie wydarzenia poprzez wypełnienie formularza podstawowymi danymi.
  
* **Historyjka użytkownika:**
  * Jako organizator,
  * chcę przejść przez formularz z jasnymi instrukcjami
  * abym mógł samodzielnie opublikować wydarzenie bez konieczności dodatkowych szkoleń i kontaktu z pomocą techniczną.

* **Cel biznesowy:** Łatwość wdrożenia. System ma być na tyle intuicyjny, aby organizatorzy mogli z niego korzystać bez potrzeby szkoleń czy kontaktu z pomocą techniczną.
  
* **Warunki wstępne:** Użytkownik jest zalogowany w systemie i posiada rolę Organizatora.

* **Warunki końcowe:** Wydarzenie zostaje poprawnie skonfigurowane, zapisane w bazie i upublicznione dla klientów.

* **Kryteria akceptacji:**
  * **WF-APLIK-02-A**: Wyświetlenie formularza tworzenia (Scenariusz główny 1)
    * Opis: System prezentuje organizatorowi formularz.
    * Kryteria akceptacji:
      * Given: Jestem zalogowany jako organizator.
      * When: Wybieram opcję "Utwórz wydarzenie".
      * Then: System wyświetla czytelny formularz, do którego można wpisać podstawowe dane dotyczące wydarzenia (opis, data, godzina, lokalizacja, cenę biletów).
        
  * **WF-APLIK-02-B**: Konfiguracja sektorów (Scenariusz główny 2)
    * Opis: Organizator dodaje sektory z wybranej areny i definiuje dla nich dostępne typy biletów oraz ich ceny.
    * Kryteria akceptacji:
      * Given: Wypełniono podstawowe dane wydarzenia.
      * When: Dodaje potrzebne sektory oraz dodaję warianty cenowe (np. Normalny - 100 PLN, Ulgowy - 50 PLN).
      * Then: System generuje mapę miejsc dla danej areny.
      * And: Przypisuje ceny do miejsc.
        
  * **WF-APLIK-02-C**: Próba publikacji wydarzenia bez uzupełnienia wymaganych pól (Scenariusz alternatywny 1)
    * Opis: System blokuje publikację wydarzenia z brakującymi danymi.
    * Kryteria akceptacji:
      * Given: Jestem w formularzu tworzenia wydarzenia.
      * And: Nie wypełniłem wszystkich wymaganych pól
      * When:  Próbuję przejść do kolejnego kroku.
      * Then: System powinien wyświetlić komunikat przy niewypełnionych polach "To pole jest wymagane".
      * And:  Uniemożliwia przejście do następnych kroków.

  * **WF-APLIK-02-D**: Wysłanie formularza wydarzenia do akceptacji (Scenariusz główny 3)
    * Opis: Organizator wypełnił cały formularz i przesyła go do akceptacji.
    * Kryteria akceptacji:
      * Given: Formularz wydarzenia i konfiguracja biletów zostały poprawnie uzupełnione.
      * When: Klikam przycisk "Wyślij do akceptacji".
      * Then: System zapisuje wydarzenie w bazie ze statusem "Oczekuje na weryfikację".
      * And: Organizator otrzymuje komunikat potwierdzający wysłanie zgłoszenia.

**WF-03: Logowanie i rejestracja użytkowników**

* **Opis:** Umożliwia tworzenie nowych kont oraz autoryzację dostępu do istniejących. 
  
* **Historyjka użytkownika:**
  * Jako nowy użytkownik,
  * chcę założyć konto oraz się zalogować,
  * abym mógł uzyskać dostęp do systemu.

* **Cel biznesowy:** Umożliwienie identyfikacji użytkowników oraz zabezpieczenie dostępu do panelu organizatora i historii zakupów użytkownika.
  
* **Warunki wstępne:** Użytkownik jest niezalogowany.

* **Warunki końcowe:** Użytkownik zostaje uwierzytelniony, system generuje token i przekierowuje do panelu głównego.

* **Kryteria akceptacji:**
  * **WF-APLIK-03-A**: Pomyślna rejestracja konta (Scenariusz główny 1)
    * Opis: Użytkownik zakłada nowe konto podając unikalny adres email.
    * Kryteria akceptacji:
      * Given: Jestem na formularzu rejestracji.
      * When: Wpisuję unikalny adres email, hasło spełniające wymogi bezpieczeństwa oraz imię i nazwisko.
      * Then: System tworzy konto w bazie danych.
      * And: Wyświetla komunikat o udanej rejestracji i przekierowuje do logowania.
        
  * **WF-APLIK-03-B**: Próba rejestracji na istniejący w systemie email (Scenariusz alternatywny)
    * Opis: System blokuje utworzenie duplikatu konta.
    * Kryteria akceptacji:
      * Given: W bazie danych istnieje już użytkownik z konkretnym adresem.
      * When: Próbuję zarejestrować nowe konto używając tego samego adresu.
      * Then: System blokuje operację.
      * And: Wyświetla komunikat "Konto z tym adresem email już istnieje".
        
  * **WF-APLIK-03-C**: Pomyślne logowanie (Scenariusz główny 2)
    * Opis: Użytkownik loguje się do systemu poprawnymi danymi.
    * Kryteria akceptacji:
      * Given: Posiadam aktywne konto w systemie.
      * When:  Wpisuję poprawny login i hasło w formularzu logowania.
      * Then: System uwierzytelnia dane i nadaje token dostępu.
      * And:  Zostaję przekierowany na stronę główną.

  * **WF-APLIK-03-D**: Błąd logowania (Scenariusz alternatywny)
    * Opis: Próba logowania przy użyciu niepoprawnego hasła lub nieistniejącego emailu.
    * Kryteria akceptacji:
      * Given: Jestem na stronie logowania.
      * When: Wpisuję poprawny email, ale błędne hasło (lub na odwrót).
      * Then: System odmawia dostępu.
      * And: Wyświetla komunikat "Błędny login lub hasło".

**WF-04: Automatyczne generowanie biletów PDF**

* **Opis:** Po poprawnym sfinalizowaniu zamówienia, system automatycznie generuje bilet w formacie PDF. Dokument zawiera kluczowe informacje o wydarzeniu oraz unikalny kod QR umożliwiający późniejszą weryfikację.
  
* **Historyjka użytkownika:**
  * Jako klient,
  * chcę otrzymać bilet w formacie PDF zaraz po dokonaniu płatności, 
  * abym mógł go łatwo zapisać na urządzeniu mobilnym lub wydrukować przed wejściem na wydarzenie.

* **Cel biznesowy:** Automatyzacja procesu dostarczania biletów.
  
* **Warunki wstępne:** Zamówienie zostało poprawnie opłacone.

* **Warunki końcowe:**: Użytkownik ma dostęp do pliku PDF, który jest zapisany w bazie danych i przypisany do jego zamówienia.

* **Kryteria akceptacji:**
  * **WF-APLIK-04-A**: Zawartość biletu (Scenariusz główny)
    * Opis: Wygenerowany plik zawiera wszystkie niezbędne dane.
    * Kryteria akceptacji:
      * Given: Zamówienie na dane wydarzenie zostało opłacone.
      * When: System generuje plik PDF.
      * Then: Dokument zawiera nazwę wydarzenia, datę i godzinę, lokalizację, numer miejsca/sektora oraz imię i nazwisko posiadacza (jeśli podano).
      * And: Na bilecie znajduje się unikalny kod QR.
        
  * **WF-APLIK-04-B**: Pobieranie pliku (Scenariusz główny 2)
    * Opis: Użytkownik może pobrać bilet z poziomu interfejsu.
    * Kryteria akceptacji:
      * Given: Plik PDF został wygenerowany.
      * When: Klikam przycisk "Pobierz bilet" na ekranie podsumowania zamówienia lub w profilu użytkownika.
      * Then: Przeglądarka rozpoczyna pobieranie pliku o rozszerzeniu .pdf.

**WF-05: Mechanizm obsługi płatności**

* **Opis:** Moduł odpowiedzialny za procesowanie transakcji finansowych. Umożliwia użytkownikowi wybór metody płatności (np. Karta płatnicza, BLIK) i sfinalizowanie zamówienia.
  
* **Historyjka użytkownika:**
  * Jako klient,
  * chcę mieć możliwość wyboru metody płatności i szybkiego opłacenia zamówienia,
  * abym mógł sfinalizować zakup i otrzymać gwarancję rezerwacji miejsc.

* **Cel biznesowy:** Automatyzacja finalizacji zamówienia, eliminująca konieczność ręcznej weryfikacji wpłat przed wysyłką biletów.
  
* **Warunki wstępne:** Użytkownik posiada zamówienie o statusie "AWAITING PAYMENT", a czas rezerwacji miejsc jeszcze nie upłynął.

* **Warunki końcowe:**: Zamówienie otrzymuje status końcowy "PAID" (co uruchamia generowanie biletów) lub w przypadku niepowodzenia generuje błąd płatności.

* **Kryteria akceptacji:**
  * **WF-APLIK-05-A**: Pomyślna realizacja transakcji (Scenariusz główny)
    * Opis: Proces płatności przebiega prawidłowo, środki zostają zaksięgowane.
    * Kryteria akceptacji:
      * Given: Użytkownik znajduje się w podsumowaniu zamówienia.
      * When: Wybiera metodę płatności i zatwierdza transakcję.
      * Then: System weryfikuje dostępność środków (autoryzacja pozytywna).
      * And: Status zamówienia w bazie danych zmienia się na "PAID".
      * And: Użytkownik zostaje przekierowany do strony potwierdzenia zakupu.
        
  * **WF-APLIK-05-B**: Odrzucenie transakcji (Scenariusz alternatywny)
    * Opis: Obsługa błędu w przypadku odrzucenia płatności przez operatora.
    * Kryteria akceptacji:
      * Given: Użytkownik próbuje opłacić zamówienie.
      * When: Autoryzacja płatności kończy się niepowodzeniem.
      * Then: System wyświetla komunikat błędu: "Transakcja odrzucona. Spróbuj ponownie lub wybierz inną metodę".
      * And: Status zamówienia pozostaje "AWAITING PAYMENT".
      * And: Miejsca pozostają zablokowane dla użytkownika do czasu wygaśnięcia czasu rezerwacji.
  
**3.1. Priorytetyzacja wymagań**

| ID Funkcji | Nazwa Funkcji | Korzyść | Kara | Koszt | Ryzyko | WYNIK | Decyzja MVP |
| :--- | :--- | :---: | :---: | :---: | :---: | :---: | :---: |
| **WF-01** | **Zakup biletu (z walidacją)** | 21 | 21 | 13 | 13 | **1.61** | **TAK** |
| **WF-02** | **Tworzenie wydarzenia** | 21 | 21 | 8 | 5 | **3.23** | **NIE** |
| **WF-03** | **Logowanie i Rejestracja** | 8 | 21 | 5 | 2 | **4.14** | **TAK** |
| **WF-04** | **Generowanie biletu PDF** | 13 | 8 | 5 | 3 | **2.62** | **TAK** |
| **WF-STAT** | **Statystyki dla organizatora** | 5 | 2 | 3 | 2 | **1.40** | **NIE** |
| **WF-MAIL** | **Powiadomienia E-mail** | 8 | 5 | 5 | 8 | **1.00** | **NIE** |
| **WF-05** | **Obsługa mechanizmu płatności** | 3 | 5 | 2 | 1 | **2.66** | **TAK** |

### 4. Atrybuty jakościowe 

Atrybuty jakościowe definiują jakość działania systemu eTicket. Ze względu na ograniczenia budżetowe (0 PLN) oraz specyfikę MVP, przyjęto następującą hierarchię ważności.

**4.1. Priorytetyzacja atrybutów**
| Atrybut Jakościowy | Opis | Uzasadnienie |
| :--- | :--- | :--- |
| **Integralność danych** | System musi gwarantować spójność danych o rezerwacjach, sprzedanych biletach. Nie może dojść do sytuacji, w której jedno miejsce zostanie sprzedane dwóm osobom. | Jest to najważniejszy atrybut. System jest bezużyteczny, jeśli sprzedaje bilety na to samo miejsce wielu osobom. |
| **Dostępność** | System powinien być dostępny dla użytkowników w momentach o największym obciążeniu. | Awaria w trakcie startu sprzedaży jest niedopuszczalna, nawet jeśli nie wymagamy pełnej dostępności 24/7. |
| **Wydajność** | Czas reakcji systemu na kluczowe operacje (np. algorytm walidacji miejsc) musi być akceptowalny dla człowieka. | Zbyt wolne działanie algorytmu walidacji miejsc zniechęci użytkowników i uniemożliwi realizację celu biznesowego (szybki zakup w mniej niż 90 sekund). |
| **Bezpieczeństwo** | Ochrona danych osobowych organizatorów i klientów zgodnie z wymogami RODO. | System przetwarza dane osobowe, co stanowi ryzyko prawne i etyczne. |
| **Użyteczność** | Intuicyjność interfejsu pozwalająca na proste tworzenie wydarzeń przez organizatorów, jak i szybki zakup biletów przez klientów. | Łączy się to z głównym celem biznesowym. Jest na 5 miejscu, bo system najpierw musi działać (1-3) i być bezpieczny (4), zanim będzie przyjemny w obsłudze |
| **Modyfikowalność** | Łatwość wprowadzania zmian w kodzie i strukturze bazy. | Projekt jest w fazie MVP, co oznacza, że kod będzie często zmieniany i możliwie rozwijany o nowe funkcje w przyszłości. |
| **Skalowalność** | Możliwość obsługi rosnącej liczby użytkowników. | Ze względu na ograniczenie budżetowe w fazie MVP, skalowalność nie jest priorytetem. |

**4.2. Mierzalna specyfikacja**

Dla trzech najważniejszych atrybutów zdefiniowano scenariusze testowe.

**Scenariusz 1: Integralność Danych**

| Element | Opis |
| :--- | :--- |
| **Źródło bodźca** | Dwóch różnych użytkowników. |
| **Bodziec** | Próba zakupu tego samego miejsca w dokładnie tym samym czasie. |
| **Artefakt** | Baza danych. |
| **Środowisko** | Normalne działanie systemu przy większym obciążeniu. |
| **Reakcja** | System wykorzystuje mechanizm transakcji. Pierwsze żądanie kończy się sukcesem, drugie zostaje odrzucone. |
| **Miara reakcji** | Występuje 0 przypadków podwójnej rezerwacji. |

**Scenariusz 2: Dostępność**

| Element | Opis |
| :--- | :--- |
| **Źródło bodźca** | Organizator wydarzenia. |
| **Bodziec** | Uruchomienie sprzedaży biletów na popularne wydarzenie, co powoduje gwałtowny wzrost ruchu. |
| **Artefakt** | Cały system. |
| **Środowisko** | Szczytowe obciążenie (zdefiniowane gdy 100 użytkowników jednocześnie próbuje kupić bilety). |
| **Reakcja** | System pozostaje dostępny i responsywny, obsługuje wszystkie żądania bez błędów serwera. |
| **Miara reakcji** | System utrzymuje dostępność na poziomie minimum 99% w czasie pierwszych 15 minut od startu sprzedaży, mierzoną jako stosunek udanych żądań HTTP do wszystkich żądań. |

**Scenariusz 3: Wydajność**

| Element | Opis |
| :--- | :--- |
| **Źródło bodźca** | Klient. |
| **Bodziec** | Wybiera 2 miejsca na mapie sektora i klika "Dalej", uruchamiając algorytm walidacji miejsc. |
| **Artefakt** | Algorytm walidacji w module rezerwacji + baza danych. |
| **Środowisko** | Szczytowe obciążenie (zdefiniowane gdy 100 użytkowników jednocześnie próbuje wybrać miejsca w tym samym sektorze). |
| **Reakcja** | Algorytm zadziała poprawnie. |
| **Miara reakcji** | Czas wykonania algorytmu wynosi maksymalnie 500ms dla 95% przypadków, mierzony od momentu wysłania żądania HTTP do otrzymania odpowiedzi z wynikiem walidacji. |

**4.3. Analiza kompromisów architektonicznych**

Realizacja powyższych celów wymaga podjęcia trudnych decyzji projektowych.

**Analiza dla Scenariusza 1: Integralność Danych**
* **Cel:** 100% spójności danych przy współbieżnych zakupach.
* **Możliwe rozwiązanie architektoniczne:** Zastosowanie pesymistycznego blokowania(for update) na poziomie bazy danych dla rekordów miejsc.
* **Kompromis:**
    * **Pozytywny:** Zwiększamy **integralność danych** - pewność, że nie sprzedamy tego samego biletu dwa razy.
    * **Negatywny:** Pogarszamy **wydajność**. Blokowanie rekordów kolejkuje zapytania. Przy dużym ruchu może to prowadzić do wolniejszego działania systemu dla użytkowników próbujących kupić bilety w tym samym sektorze, jednak bezpieczeństwo transakcji jest ważniejsze.

**Analiza dla Scenariusza 2: Dostępność**
* **Cel:** Zapewnienie dostępności systemu w krytycznym momencie - start sprzedaży.
* **Możliwe rozwiązanie architektoniczne:** Umieszczenie strony internetowej (React) i serwera (Spring Boot) na dwóch osobnych, niezależnych hostingach
* **Kompromis:**
    * **Pozytywny:** Zyskujemy ochronę zasobów serwera. W momencie, gdy użytkownicy masowo odświeżają stronę, cały ten ruch obsługuje hosting frontendowy. Zwiększamy też satysfakcję użytkowników, gdyż mogą kupić bilety bez irytacji.
    * **Negatywny:** Akceptujemy ryzyko niespójności interfejsu. Jeśli backend ulegnie awarii, strona internetowa nadal będzie działać, ale każda próba zakupu skończy się błędem.

**Analiza dla Scenariusza 3: Wydajność**
* **Cel:** Osiągnięcie czasu odpowiedzi poniżej 500ms.
* **Możliwe rozwiązanie architektoniczne:** Zastosowanie lekkich obiektów DTO do pobierania stanu sektorów zamiast operowania na pełnych encjach bazodanowych.
* **Kompromis:**
    * **Pozytywny:** Znacząco poprawiamy **wydajność**. Przesyłamy z bazy tylko to co nam potrzebne, pomijając zbędne szczegóły, co odciąża sieć i RAM.
    * **Negatywny:** Zwiększamy nakład pracy programistycznej. Musimy napisać dodatkowe klasy i interfejsy mapujące.


### 5. Odkrywanie i Analiza Wymagań

**5.1. Analiza porównawcza**

Celem analizy jest porównanie systemu eTicket z najlepszymi praktykami rynkowymi w celu identyfikacji mocnych i słabych stron oraz znalezienia inspiracji do ulepszeń. 

  * **5.1.1. Identyfikacja konkurencji**
    * **Konkurencja bezpośrednia:**
        * **Eventbrite** - jedna z najbardziej znanych platform, umożliwia samodzielne stworzenie wydarzenia.
        * **Ticketmaster/eBilet** - systemy profesjonalne obsługujące duże obiekty z numerowanymi miejscami.
    * **Wzorce funkcjonalne:**
        * **Systemy kinowe Cinema City** - zamknięte systemy sprzedaży biletów z wyborem miejsc.

  * **5.1.2. Zdefiniowanie kryteriów oceny:**
  
    | Kryterium | Kryterium oceny | Eventbrite | Ticketmaster/eBilet | Cinema City
    | :--- | :--- | :--- | :--- | :--- | 
    | **Łatwość tworzenia wydarzenia** | Czas potrzebny na utworzenie i opublikowanie wydarzenia + intuicyjność interfejsu| trwa 10 min | wymaga kontaktu z działem sprzedaży | Nie dotyczy |
    | **Wybór miejsc na mapie** | Obecność funkcji wizualizacji sali z możliwością wyboru konkretnych miejsc | Nie | Tak | Tak |
    | **Inteligentna walidacja miejsc** | System zapobiega powstawaniu pojedynczych wolnych miejsc | Nie | Tak | Tak |
    | **Intuicyjność procesu zakupu** | Prostota i zrozumiałość ścieżki zakupowej dla klienta | 4/5 | 4/5 | 5/5 |
    | **Czas zakupu biletu** | Średni czas od wejścia na stronę wydarzenia do potwierdzenia zakupu | ~120 sekund | ~180 sekund | ~60 sekund |
    | **Model cenowy** | Koszt dla organizatora - prowizje, opłaty stałe | Brak opłat za bezpłatne wydarzenia, na płatne prowizja 6.6% + 1,79 USD | Prowizja 10-15% | Nie dotyczy |
    | **Generowanie biletów PDF** | Automatyczne tworzenie biletów PDF z kodem QR/kodem kreskowym | Tak | Tak | Tak |
    | **Statystyki dla organizatora** | Dostęp do raportów sprzedaży i analiz| Tak | Tak | Nie dotyczy|
    | **Wsparcie techniczne** | Jakość i dostępność pomocy dla użytkowników| Email + czat | Dedykowany opiekun | Infolinia |

  * **5.1.3. Synteza wyników:**
  
    * **Co konkurencja robi dobrze oraz jej unikalne funkcje**
      * **Cinema City:** Udowadnia, że wprowadzenie walidacji miejsc nie spowalnia znacznie zakupu użytkownika.
      * **Eventbrite:** Oferuje świetny model samoobsługi. Dostęp do statystyk i automatyczne generowanie biletów PDF jest standardem rynkowym, którego oczekują organizatorzy.
      * **Ticketmaster:** Profesjonalna wizualizacja mapy miejsc. Przy wyborze miejsc należy pozostawić co najmniej dwa sąsiadujące wolne miejsca.
    
    * **Słabe punkty konkurencji**
      * Eventbrite w modelu samoobsługowym nie posiada algorytmu, który zapobiega powstawaniu pojedynczych wolnych miejsc. Prowadzi to do strat finansowych organizatora. Natomiast większe systemy np. Ticketmaster są niedostępne dla małych organizatorów np. startupów – wymagają umów i długiego wdrażania.
