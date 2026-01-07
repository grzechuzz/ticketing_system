TODO
1.2.3 tam był dodal że nie liczba wolnych miejsc, tylko pojedynczych??? wiesz ocb
1.2.2 Okreslic czego system nie bedzie robil
* **Poza Zakresem:** Jasno określcie, czego system nie będzie robił, aby uniknąć nieporozumień.
1.3 aktualizować słowniczek na bieżąco
1.4 co zawierają kolejne rozdziały na koniec sie zrobi

## Tytuł zmienic

### 1. Wstęp

**1.1. Cel:**
Dokument opisuje wymagania dla systemu eTicket w wersji 1.0, który został stworzony jako platforma do zarządzania sprzedażą biletów na wydarzenia.
Przeznaczony on jest dla zespołu deweloperskiego, testerów, klientów, zarządu, sponsorów.
Służy jako:
- umowa pomiędzy klientem a zespołem deweloperskim
- fundament do tworzenia stuktury systemu
- baza do tworzenia testów i weryfikacji kryteriów akceptacji
- narzędzie do zarządzania/zmieniania projektu
- 
<br>

**1.2. Wizja Produktu:**
Każdy organizator bez względu na swoje możliwości finansowe i rozmiar ma prawo do prostego i bezkosztowego rozwiązania, które pozwoli mu skupić się na organizacji wydarzeń, a nie nauce nowej technologii.
Z tego powodu przychodzimy z opracowaniem prostego i łatwego w obsłudze narzędzia eTicket 

**1.2.2. Zakres Produktu:**
Nasz system będzie platformą, która umożliwia:
- Tworzenie i publikowanie wydarzeń przez organizatorów
- Zarządzanie wydarzeniami w tym liczbą dostępnych biletów
- Rejestrowanie i przetwarzanie transakcji łącznie z wysyłaniem potwierdzeń zakupu
- Zarządzanie kontami i uprawnieniami
- 
Natomiast nie będziemy mieli opcji ...........

**1.2.3. Cele Produktu:**
Główne cele biznesowe:
- Skrócenie czasu wdrożenia organizatorów. System ma umożliwiać utworzenie i opublikowanie wydarzenia w czasie krótszym niż 5 minut.
Kryterium akceptacji: 80% nowych organizatorów pomyślnie publikuje swoje pierwsze wydarzenie w czasie mniejszym niż 5 minut w pierwszym miesiącu od uruchomienia produktu 80%.
- Zwiększenie efektywności sprzedaży miejsc siedzących. System ma minimalizować liczbę niesprzedanych pojedynczych miejsc poprzez inteligentną alokację miejsc.
Kryterium akceptacji: Liczba pozostawionych wolnych miejsc w sektorach siedzących jest mniejsza niż 5% całkowitej liczby miejsc w tych sektorach po zakończeniu sprzedaży wydarzenia.

Główne cele użytkowników:
- Szybkie utworzenie wydarzenia bez konieczności dodatkowego szkolenia. System ma poprowadzić użutkownika krok po kroku wraz z wskazówkami na każdym etapie.
Kryterium akceptacji: W grupie testowej 10 osób niewidzących wcześniej projektu, co najmniej 8 musi ukończyć proces tworzenia wydarzenia poniżej 5 minut.
- DURIG CEL UZYTKOWNIKA

<br>

**1.3. Definicje, Akronimy i Skróty:**

Organizator - użytkownik systemu posiadający uprawnienia do tworzenia i zarządzania wydarzeniami


<br>

**1.4. Przegląd Dokumentu:**


<br>


### 2. Opis ogólny

**2.1. Główne funkcje produktu**
- **Zarządzanie wydarzeniami**: utworzenie wydarzenia oraz jego publikacja
- **Przegląd wydarzeń**: lista wydarzeń i szczegółowy widok planu miejsc
- **Zakup biletów**: system umożliwia klientowi zakup biletów na różne typy wydarzeń
- **Inteligentna alokacja miejsc**: w przypadku zakupu biletów z przypisanym miejscem system może automatycznie podpowiadać miejsca, stosując politykę minimalizacji pozostawiania pojedynczych wolnych miejsc
- **Generowanie dokumentów**: automatyczne tworzenie dokumentów w formacie PDF, takich jak bilety z kodami QR oraz faktury
- **Obsługa płatności**: przetwarzanie transakcji zakupu poprzez integrację z zewnętrznym operatorem płatności
  
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

* **2.3.3. Ograniczenia biznesowe:**

  **Ograniczenie:**
  Całkowity budżet na hosting, infrastrukturę i zewnętrzne usługi wynosi 0 PLN miesięcznie.
  
  **Źródło:**
  Projekt akademicki realizowany bez zewnętrznego finansowania.
  
  **Wpływ na architekturę:**
  * Konieczność doboru infrastruktury opartej wyłącznie o darmowe plany usług
  * Ograniczenia w zakresie wydajności

* **2.3.4. Ograniczenie prawne:**
  
  **Ograniczenie:**
  System musi być zgodny z Rozporządzeniem o Ochronie Danych Osobowych (RODO), a wszystkie dane osobowe użytkowników (organizatorów i uczestników) muszą być fizycznie przechowywane na serwerach zlokalizowanych w granicach Europejskiego Obszaru Gospodarczego (EOG).
  
  **Źródło:**
  Prawo Unii Europejskiej.
  
  **Wpływ na architekturę:**
  * Drastycznie zawęża wybór dostawców usług chmurowych do tych, którzy posiadają centra danych w EOG
  * Wymusza implementację mechanizmów do obsługi praw użytkowników (prawo do bycia zapomnianym, prawo do eksportu danych), co musi być uwzględnione w projekcie bazy danych i API
  * Narzuca konieczność anonimizacji danych w środowiskach deweloperskich i testowych

**2.4. Założenia projektowe**

**2.4.1. Założenie dotyczące użyteczności**
*   **Założenie:** Zakładamy, że interfejs użytkownika będzie tak łatwy do zrozumienia, że 80% nowych organizatorów (bez wcześniejszego szkolenia) będzie w stanie utworzyć i opublikować wydarzenie w czasie krótszym niż 5 minut.
*   **Ryzyko:** Jeśli okaże się, że interfejs jest zbyt skomplikowany to cel biznesowy projektu (zapewnienie prostego narzędzia) nie zostanie osiągnięty, co poskutkuje zniechęceniem użytkowników, rezygnacją z platformy.
*   **Plan walidacji:**
    *   **Co:** Przeprowadzenie testów użyteczności z pomiarem czasu.
    *   **Jak:** Przeprowadzenie scenariusza testowego na grupie 10 osób nieznających systemu. Użytkownicy otrzymują dane wydarzenia i muszą je wprowadzić. Mierzymy czas stoperem.
    *   **Kiedy:** Przed finalnym wydaniem projektu, po stworzeniu funkcjonalnego prototypu interfejsu.
    *   **Kto:** Jeden z testerów.
<br>

**2.4.2. Założenie algorytmiczne**
*   **Założenie:** Zakładamy, że algorytm przypisywania miejsc, który będzie automatycznie przypisywał miejsca siedzące kupującym w sposób unikający powstawania pojedynczych wolnych miejsc, pozostawi mniej niż 5% wolnych miejsc w wyprzedanych sektorach
*   **Ryzyko:** Jeśli algorytm okaże się nieskuteczny i będzie pozostawiać liczne pojedyncze wolne miejsca między zajętymi, organizatorzy stracą potencjalny przychód ze sprzedaży biletów. Dla użytkowników może to prowadzić do porzucenia koszyka zakupowego w sytuacji, gdy wymuszony jest wybór miejsc obok kogoś, gdy wolą odstęp
*   **Plan walidacji:**
    *   **Co:** Symulacja procesu sprzedaży biletów.
    *   **Jak:** Napisanie testu, który generuje 1000 losowych prób zakupu biletów dla różnych konfiguracji tj. pojedyncze bilety, dla 3-5 osobowych grup itp., przestrzegając zaimplementowanych reguł. Na koniec skrypt zlicza procent niesprzedanych miejsc.
    *   **Kiedy:** Przed rozpoczęciem prac nad warstwą wizualną wyboru miejsc.
    *   **Kto:** Developer odpowiedzialny za moduł rezerwacji.

### 3. Wymagania Funkcjonalne
