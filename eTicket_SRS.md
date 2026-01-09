

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

**1.3. Definicje, Akronimy i Skróty:**

Organizator - użytkownik systemu posiadający uprawnienia do tworzenia i zarządzania wydarzeniami

**1.4. Przegląd Dokumentu:**


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
  *   **Ryzyko:** Klienci moga odebrać tę blokadę jako błąd systemu lub irytujące ograniczenie ich swobody, co doprowadzi do porzucenia koszyka i spadku sprzedaży.
  *   **Plan walidacji:**
      *   **Co:** Testy użyteczności na prototypie.
      *   **Jak:** Przeprowadzenie krótkich sesji z 10 użytkownikami, którzy otrzymują zadanie wybrania miejsca w sposób generujący "lukę". Obserwacja czy po otrzymaniu komunikatu blokady rozumieją go i korygują wybór czy rezygnują z zakupu.
      *   **Kiedy:** Na etapie projektowania interfejsu.
      *   **Kto:** Zespół projektowy.

* **2.4.2. Założenie dotyczące praw autorskich**
  *   **Założenie:** Zakładamy, że organizatorzy będą publikować wyłącznie materiały graficzne do których posiadają prawa autorskie, co zwalnia system z konieczności implementacji automatycznych mechanizmów weryfikacji własności intelektualnej. 
  *   **Ryzyko:** Właściciele systemu mogą zostać pociagnięci do współodpowiedzialności prawnej.
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
    * Opis: Użytkownik robi lukę, ale w jego rzędzie nie ma alternatywych miejsc, są jednak w rzędzie wyżej lub niżej.
    * Kryteria akceptacji:
      * Given: Wybieram miejsca z luką w rzędzie 5.
      * And: W rzędzie 5 brakuje innej możliwości wyboru miejsc obok siebie dla tej liczby osób.
      * And: Algorytm wykrywa, że w rzędzie sąsiednim (4 lub 6) dostepne są wolne miejsca obok siebie nietworzące luki.
      * When: Klikam "Dalej".
      * Then: System blokuje przejście.
      * And: Wyświetla komunikat: "Ten wybór zostawia pojedyncze wolne miejsce. Dostępne są inne miejsca obok siebie." i podpowiada sugerowane miejsca w innym rzędzie.

  * **WF-APLIK-01-D**: Wybór miejsc tworzących lukę (Scenariusz alternatywny 3)
    * Opis: Użytkownik robi lukę, a system po przeszukaniu rzędu obecnego oraz sąsiednich nie znajduje nic lepszego.
    * Kryteria akceptacji:
      * Given: Wybieram miejsca tworząc lukę.
      * And: System sprawdza rząd obecny, wyższy i niższy i nie znajduje żadnych lepszych miejsc.
      * When: Klikam "Dalej".
      * Then: System zezwala na wybór (mimo luki), uznając, że klient nie ma innej opcji i przechodzi dalej.
        
  * **WF-APLIK-01-E**: Konflikt rezerwacji (Scenariusz wyjątkowy)
      * Opis: Użytkownik wybiera miejsce, ale w tej samej chwili zajmuje inny klient
      * Kryteria akceptacji:
        * Given: Widzę wybrane miejsce jako wolne
        * When: Klikam na to miejsce, jednak serwer przetworzył żądanie drugiego klienta ułamek sekundy wcześniej.
        * Then: System blokuje moją rezerwację i wyświetla komunikat: "To miejsce jest niedostępne".
    
      
  
     


