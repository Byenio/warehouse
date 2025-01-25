# Dokumentacja projektu Warehouse

# Wstęp

Celem projektu jest stworzenie pełnej aplikacji webowej ***Warehouse*** symulującej działanie magazynu produktów.

Temat projektu został wybrany, ponieważ łączy wiedzę zdobytą na laboratoriach z jej praktycznym zastosowaniem w rzeczywistym scenariuszu (*real-case scenario*).

Backend aplikacji (część serwerowa) został zrealizowany w frameworku języka **Java** - **Spring Boot**. Aplikacja używa bazy danych **MongoDB** do przechowywania danych.

Frontend aplikacji (część kliencka) został zrealizowany w frameworku języka **JavaScript** (**TypeScript**) - **NextJS**.

# Opis szczegółowy

## Wybór technologicznego stacku

Poniżej opisane jest rozumowanie stojące za wyborem technologii użytych w projekcie.

### Backend

API aplikacji zostało zrealizowane w języku Java, co było wymogiem projektu tworzonego w ramach przedmiotu **Programowanie III**.

Aby przyspieszyć prace nad projektem, do stworzenia API wykorzystano framework **Spring Boot**, który upraszcza m.in. tworzenie endpointów w specyfikacji protokołu HTTP.

Jako system zarządzania bazą danych wybrano MongoDB. Jego wybór podyktowany był chęcią nauki obsługi nierelacyjnej bazy danych.

### Frontend

Klient dla wykonywanego API został zrealizowany w frameworku NextJS, który charakteryzuje się bardzo dobrym wsparciem dla tzw. komponentów serwerowych. Te z kolei idealnie nadają się do pobierania danych z API w izolacji od klienta, do którego przesyłany jest już jedynie wynik zapytań.

## Modele danych

Z racji, że MongoDB nie posiada bezpośrednio tabel i relacji, należało utworzyć modele w postaci klas odpowiadające strukturom wykorzystanych danych. Jedyne co istnieje w bazie danych to tzw. kolekcje, do których przyłączone są poszczególne klasy.

Poniżej opisane są wszystkie adnotacje wykorzystywane w definicjach klas:

- @Document - definiuje kolekcję, której model definiuje klasa,
- @Data - adnotacja, która definiuje kilka pomocnych metod w klasie (np. gettery, settery, toString, equals, hashCode) - dzięki temu redukowana jest ilość powtarzalnego kodu w każdej klasie,
- @AllArgsConstructor - adnotacja, która tworzy konstruktor klasy z wszystkimi argumentami,
- @NoArgsConstructor - adnotacja, która tworzy konstruktor klasy z brakiem argumentów.
- @Id - jest informacją dla MongoDB, że pole poniżej jest identyfikatorem obiektu w kolekcji.

Poniżej opisane są wszystkie modele danych:

### Warehouse

Klasa **Warehouse** reprezentuje obiekt magazynu w przypadku, gdyby klient chciał zarządzać więcej niż jednym.

```java
@Document(collection = "warehouse")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Warehouse {

    @Id
    private String id;
    private String name;
    private String imageUrl;

    private List<String> productsId = new ArrayList<>();

}
```

- w polu *imageUrl* przechowywany jest link do okładki karty magazynu,
- w polu *productsId* przechowywana jest lista produktów należących do magazynu.

### Country

Klasa **Country** reprezentuje obiekt kraju, z którego pochodzi producent towaru.

```java
@Document(collection = "country")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Country {

    @Id
    private String id;
    private String name;
    private String code;

    private List<String> manufacturersId = new ArrayList<>();

}
```

- w polu *code* przechowywany jest numer kraju potrzebny do weryfikacji kodu kreskowego produktu,
- w polu *manufacturersId* przechowywana jest lista identyfikatorów producentów zarejestrowanych w kraju.

### Manufacturer

Klasa **Manufacturer** reprezentuje obiekt producenta produktów.

```java
@Document(collection = "manufacturer")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Manufacturer {

    @Id
    private String id;
    private String name;
    private String description;
    private String code;
    private String logoUrl;
    private String website;

    private String countryId;
    private List<String> productsId = new ArrayList<>();

}
```

- w polu *code* przechowywany jest numer producenta potrzebny do weryfikacji kodu kreskowego produktu,
- w polu *countryId* przechowywany jest identyfikator kraju, w którym zarejestrowany jest producent,
- w polu *productsId* przechowywana jest lista produktów producenta.

### Category

Klasa **Category** reprezentuje obiekt kategorii produktu.

```java
@Document(collection = "category")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Category {

    @Id
    private String id;
    private String name;
    private String description;

    private List<String> subcategoriesId = new ArrayList<>();

}
```

- w polu *subcategoriesId* przechowywana jest lista podkategorii, które połączone są z produktami.

### Subcategory

Klasa **Subcategory** reprezentuje obiekt podkategorii produktu.

```java
@Document(collection = "subcategory")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Subcategory {

    @Id
    private String id;
    private String name;
    private String description;

    private String categoryId;
    private List<String> productsId = new ArrayList<>();

}
```

- w polu *categoryId* przechowywany jest identyfikator kategorii, do której należy podkategoria,
- w polu *productsId* przechowywana jest lista produktów, które należą do podkategorii.

### Product

Klasa **Product** reprezentuje obiekt produktu.

```java
@Document(collection = "product")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    @Id
    private String id;
    private String name;
    private String barcode;
    private String description;
    private String imageUrl;
    private Integer netPriceInCents;
    private Float vatPercentage;
    private Integer grossPriceInCents;
    private Integer stock;

    private String subcategoryId;
    private String manufacturerId;
    private String warehouseId;

}
```

- w polu *barcode* przechowywana jest liczbowa reprezentacja kodu kreskowego, zgodna ze standardem EAN-13, gdzie:
    - cyfry [0-2] to kod kraju, z którego pochodzi produkt,
    - cyfry [3-7] to kod producenta, który wyprodukował produkt,
    - cyfry [8-12] to kod produktu,
    - cyfra [13] to cyfra kontrolna spełniająca wzór:

      $$
      C = \left(10 - \left(\sum_{i=1}^{12} D_i \cdot W_i \mod 10\right)\right) \mod 10
      $$

- w polu *imageUrl* przechowywany jest link do obrazu produktu,
- w polu *netPriceInCents* przechowywana jest wartość netto produktu w centach (Integer),
- w polu *vatPercentage* przechowywana jest wysokość podatku VAT w procentach (Float),
- w polu *grossPriceInCents* przechowywana jest wartość brutto produktu w centach (Integer),
- w polu *stock* przechowywany jest stan magazynowy produktu,
- w polu *subcategoryId* przechowywany jest identyfikator podkategorii, do której należy produkt,
- w polu *manufacturerId* przechowywany jest identyfikator producenta produktu,
- w polu *warehouseId* przechowywany jest identyfikator magazynu, do którego należy produkt.

## Reprezentacje odpowiedzi na zapytania

Przed przejściem do endpointów, należy zaznajomić się z klasową reprezentacją odpowiedzi API na zapytania klienta, ponieważ nieznacznie różnią się od modeli danych. Dzięki temu można rozwiązać potrzebne identyfikatory na obiekty oraz wybierać pola z klas. Oszczędzamy również zapytania na rozwiązywanie identyfikatorów podobiektów w późniejszym czasie.

Takie reprezentacje otrzymały jedynie najbardziej istotne klasy.

### CategoryResponse

Klasa **CategoryResponse** rozwiązuje listę identyfikatorów podkategorii na obiekty klasy **SubcategoryInfo**.

```java
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryResponse {

    @Id
    private String id;
    private String name;
    private String description;

    private List<SubcategoryInfo> subcategories;

}
```

### SubcategoryInfo

```java
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubcategoryInfo {

    @Id
    private String id;
    private String name;
    private String description;

}
```

### ProductResponse

Klasa **ProductResponse** rozwiązuje identyfikator podkategorii na obiekt klasy **SubcategoryInfo**, identyfikator producenta na obiekt klasy **ManufacturerInfo** oraz omija nieistotne pola (np. *vatPercentage*)

```java
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {

    @Id
    private String id;
    private String name;
    private String description;

    private String barcode;
    private String imageUrl;
    private Integer netPriceInCents;
    private Integer grossPriceInCents;
    private Integer stock;

    private SubcategoryInfo subcategory;
    private ManufacturerInfo manufacturer;

}
```

### SubcategoryInfo

```java
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubcategoryInfo {

    @Id
    private String id;
    private String name;
    private String description;

}
```

### ManufacturerInfo

Dodatkowo klasa **ManufacturerInfo** rozwiązuje identyfikator kraju na obiekt klasy **CountryInfo**.

```java
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ManufacturerInfo {

    @Id
    private String id;
    private String name;
    private String description;
    private String code;
    private String logoUrl;
    private String website;

    private CountryInfo country;

}
```

### CountryInfo

```java
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CountryInfo {

    @Id
    private String id;
    private String name;
    private String code;

}
```

## Kontrolery

Każdy model ma swój kontroler, który obsługuje endpointy w formacie RESTowego API.

Zapytanie w takim schemacie wygląda następująco:

- klient wykonuje request na wybrany adres API (np. /api/product/<id_produktu>)
- przy tym wybiera odpowiednią metodę HTTP:
    - GET - pobiera dane z zasobu
    - POST - przesyła dane do zasobu
    - DELETE - usuwa dane z zasobu
- serwer zwraca odpowiednie dane, komunikat oraz odpowiedni kod HTTP:
    - 200 - OK, zapytanie wykonano pomyślnie
    - 201 - CREATED, pomyślnie stworzono nowe zasoby
    - 204 - NO_CONTENT, pomyślnie usunięto zasoby
    - 400 - BAD_REQUEST, błędne dane zapytania
    - 404 - NOT_FOUND, nie znaleziono zasobu

### Przykład kontrolera

Poniżej opisany został kontroler **ProductController**.

- @RestController - adnotacja, która informuje, że jest to kontroler RESTowego API
- @RequestMapping - adnotacja, która mapuje kontroler do adresu */api/product*

Następnie tworzony jest konstruktor klasy kontrolera, który przyjmuje i zapisuje repozytoria klas modeli danych. Repozytoria umożliwiają wykonywanie operacji na bazie danych.

```java
@RestController
@RequestMapping({"/api/product"})
public class ProductController {

    private final ProductRepository productRepository;
    private final SubcategoryRepository subcategoryRepository;
    private final ManufacturerRepository manufacturerRepository;
    private final CountryRepository countryRepository;
    private final WarehouseRepository warehouseRepository;

    public ProductController(
            final ProductRepository productRepository,
            final SubcategoryRepository subcategoryRepository,
            final ManufacturerRepository manufacturerRepository,
            final CountryRepository countryRepository,
            final WarehouseRepository warehouseRepository
    ) {
        this.productRepository = productRepository;
        this.subcategoryRepository = subcategoryRepository;
        this.manufacturerRepository = manufacturerRepository;
        this.countryRepository = countryRepository;
        this.warehouseRepository = warehouseRepository;
    }

// wszystkie endpointy kontrolera

}
```

### Przykład enpointu

Poniżej opisany został endpoint **create** metody POST kontrolera produktów - pozwala on na tworzenie produktu z danymi wysłanymi w ciele zapytania.

- @PostMapping - adnotacja, która informuje o wykorzystanej metodzie HTTP - POST
- @Transactional - adnotacja, która informuje, że to zapytanie staje się transakcją i w razie niepowodzenia w trakcie - nie wykonuje się na bazie danych
- ResponseEntity<?> - typem każdej z funkcji jest ResponseEntity nieznanego od początku typu, to pozwala zwrócić zarówno dane, błędy jak i ich statusy HTTP
- @RequestBody - adnotacja, która informuje, że argumentem funkcji jest zawartość zapytania

Na początku sprawdzana jest poprawność prostych pól takich jak: *name*, *description*, *imageUrl* czy *vatPercentage*. Jeśli te dane nie istnieją w zapytaniu lub są puste, zwracany jest komunikat o błędnych danych wraz z kodem **BAD_REQUEST**.

Następnie sprawdzana jest poprawność podania ceny; w celu usprawnienia procesu obliczania ceny netto i brutto, możliwe jest przysłanie tylko jednej z nich.

W kolejnym kroku walidowane są identyfikatory podkategorii i producenta. Jeśli obiekty o przysłanych identyfikatorach nie istnieją, zwracany jest komunikat o błędnych danych wraz z kodem **NOT_FOUND**. Jeśli istnieją, produkt dodawany jest do list w obiekcie podkategorii i producenta.

Po drodze sprawdzana jest poprawność wprowadzenia kodu kreskowego. Pierwsza sprawdzana jest unikatowość kodu, jeśli nie jest unikatowy, nowy, losowy zostaje wygenerowany. Następnie kod kreskowy jest walidowany według standardu EAN-13. Funkcja generująca i walidująca opisane są niżej w dokumentacji.

Na koniec, aktualizowane są wszystkie obiekty klas zależnych oraz zwracany jest obiekt stworzonego produktu wraz z kodem **CREATED**.

```java
    @PostMapping
    @Transactional
    public ResponseEntity<?> create(@RequestBody final Product product) {
        if (product.getName() == null || product.getName().isEmpty()) {
            return new ResponseEntity<>("Name cannot be empty", HttpStatus.BAD_REQUEST);
        }

        if (product.getDescription() == null || product.getDescription().isEmpty()) {
            return new ResponseEntity<>("Description cannot be empty", HttpStatus.BAD_REQUEST);
        }

        if (product.getImageUrl() == null || product.getImageUrl().isEmpty()) {
            return new ResponseEntity<>("Image Url cannot be empty", HttpStatus.BAD_REQUEST);
        }

        if (product.getVatPercentage() == null) {
            return new ResponseEntity<>("Vat percentage cannot be empty", HttpStatus.BAD_REQUEST);
        }

        boolean hasNetPrice = product.getNetPriceInCents() != null;
        boolean hasGrossPrice = product.getGrossPriceInCents() != null;

        if (hasNetPrice && hasGrossPrice) {
            return new ResponseEntity<>("You must provide only net price or only gross price", HttpStatus.BAD_REQUEST);
        }

        if (!hasNetPrice && !hasGrossPrice) {
            return new ResponseEntity<>("Either net price or gross price must be provided", HttpStatus.BAD_REQUEST);
        }

        if (hasNetPrice) {
            int grossPriceInCents = Math.round(product.getNetPriceInCents() * (1 + product.getVatPercentage() / 100));
            product.setGrossPriceInCents(grossPriceInCents);
        } else {
            int netPriceInCents = Math.round(product.getGrossPriceInCents() / (1 + product.getVatPercentage() / 100));
            product.setNetPriceInCents(netPriceInCents);
        }

        if (product.getStock() == null) {
            return new ResponseEntity<>("Stock cannot be null", HttpStatus.BAD_REQUEST);
        }

        Product savedProduct = productRepository.save(product);

        if (product.getSubcategoryId() == null || product.getSubcategoryId().isEmpty()) {
            product.setSubcategoryId(null);
            return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
        }

        ObjectId subcategoryId = new ObjectId(product.getSubcategoryId());
        Optional<Subcategory> subcategoryEntity = subcategoryRepository.findById(subcategoryId);

        if (subcategoryEntity.isEmpty()) {
            return new ResponseEntity<>("Subcategory not found", HttpStatus.NOT_FOUND);
        }

        Subcategory subcategory = subcategoryEntity.get();
        subcategory.getProductsId().add(product.getId());
        subcategoryRepository.save(subcategory);

        ObjectId manufacturerId = new ObjectId(product.getManufacturerId());
        Optional<Manufacturer> manufacturerEntity = manufacturerRepository.findById(manufacturerId);

        if (manufacturerEntity.isEmpty()) {
            return new ResponseEntity<>("Manufacturer not found", HttpStatus.NOT_FOUND);
        }

        Manufacturer manufacturer = manufacturerEntity.get();

        Optional<Country> countryEntity = countryRepository.findById(new ObjectId(manufacturer.getCountryId()));

        if (countryEntity.isEmpty()) {
            return new ResponseEntity<>("Country not found", HttpStatus.NOT_FOUND);
        }

        Country country = countryEntity.get();

        EAN13Generator generator = new EAN13Generator();
        String currentBarcode = product.getBarcode();

        do {
            Optional<Product> productEntity = productRepository.findByBarcode(currentBarcode);

            if (productEntity.isEmpty()) {
                product.setBarcode(currentBarcode);
                break;
            }

            currentBarcode = generator.GenerateBarcode(country.getCode(), manufacturer.getCode());

        } while (true);

        productRepository.save(savedProduct);

        EAN13Validator validator = new EAN13Validator();

        if (!validator.ValidateBarcode(product.getBarcode(), country.getCode(), manufacturer.getCode())) {
            return new ResponseEntity<>("Barcode is invalid, please refer to EAN13", HttpStatus.BAD_REQUEST);
        }

        manufacturer.getProductsId().add(product.getId());
        manufacturerRepository.save(manufacturer);

        ObjectId warehouseId = new ObjectId(product.getWarehouseId());
        Optional<Warehouse> warehouseEntity = warehouseRepository.findById(warehouseId);

        if (warehouseEntity.isEmpty()) {
            return new ResponseEntity<>("Warehouse not found", HttpStatus.NOT_FOUND);
        }

        Warehouse warehouse = warehouseEntity.get();
        warehouse.getProductsId().add(product.getId());
        warehouseRepository.save(warehouse);

        return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
    }
```

### Funkcja walidująca EAN-13

Funkcja przyjmuje jako argumenty:

- kod kreskowy; w postaci String
- kod kraju producenta; w postaci String
- kod producenta; w postaci String

Funkcja zwraca:

- równość obliczonej cyfry kontrolnej z cyfrą kontrolną w przesłanym kodzie; w postaci boolean

```java
public class EAN13Validator {
    public boolean ValidateBarcode(String barcode, String countryCode, String manufacturerCode) {
        if (barcode == null || barcode.length() != 13 || !barcode.matches("\\d+")) return false;

        String barcodeCountry = barcode.substring(0, 3);
        if (!barcodeCountry.equals(countryCode)) return false;

        String barcodeManufacturer = barcode.substring(3, 8);
        if (!barcodeManufacturer.equals(manufacturerCode)) return false;

        int sum = 0;
        for (int i = 0; i < 12; i++) {
            int digit = Character.getNumericValue(barcode.charAt(i));
            sum += (i % 2 == 0) ? digit : digit * 3;
        }

        int checkDigit = (10 - (sum % 10)) % 10;

        return checkDigit == Character.getNumericValue(barcode.charAt(12));
    }
}
```

### Funkcja generująca EAN-13

Funkcja przyjmuje jako argumenty:

- kod kraju producenta; w postaci String
- kod producenta; w postaci String

Funkcja zwraca:

- wygenerowany kod kreskowy; w postaci String

```java
public class EAN13Generator {
    public String GenerateBarcode(String countryCode, String manufacturerCode) {
        String SALTCHARS = "1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();

        while (salt.length() < 4) {
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }

        String barcode = String.format("%s%s%s", countryCode, manufacturerCode, salt);

        int sum = 0;
        for (int i = 0; i < 12; i++) {
            int digit = Character.getNumericValue(barcode.charAt(i));
            sum += (i % 2 == 0) ? digit : digit * 3;
        }

        int checkDigit = (10 - (sum % 10)) % 10;
        System.out.println(barcode + checkDigit);
        return String.format("%s%s", barcode, checkDigit);
    }
}
```

## Endpointy

### /api/warehouse

| endpoint | metoda HTTP | nazwa funkcji | argumenty funkcji | zwrot funkcji | status HTTP |
| --- | --- | --- | --- | --- | --- |
| /api/warehouse | POST | create | obiekt klasy Warehouse | obiekt klasy Warehouse | 201 |
| /api/warehouse | GET | getAll | - | lista obiektów klasy Warehouse | 200 |
| /api/warehouse/{id} | GET | getById | obiekt klasy ObjectId, pobrany z URL | obiekt klasy Warehouse | 200 |
| /api/warehouse/{id} | DELETE | delete | obiekt klasy ObjectId, pobrany z URL | ciąg “Warehouse deleted” | 204 |
| /api/warehouse/{id}/products | GET | getProducts | obiekt klasy ObjectId, pobrany z URL | lista identyfikatorów produktów | 200 |
| /api/warehouse/{id}/products | POST | addProducts | obiekt klasy ObjectId, pobrany z URL;
lista identyfikatorów produktów | obiekt klasy Warehouse | 201 |
| /api/warehosue/{id}/products | DELETE | deleteProducts | obiekt klasy ObjectId, pobrany z URL;
lista identyfikatorów produktów | obiekt klasy Warehouse | 204 |

### /api/country

| endpoint | metoda HTTP | nazwa funkcji | argumenty funkcji | zwrot funkcji | status HTTP |
| --- | --- | --- | --- | --- | --- |
| /api/country | POST | create | obiekt klasy Country | obiekt klasy Country | 201 |
| /api/country | GET | getAll | - | lista obiektów klasy Country | 200 |
| /api/country/{id} | GET | getById | obiekt klasy ObjectId, pobrany z URL | obiekt klasy Country | 200 |
| /api/country/{id} | DELETE | delete | obiekt klasy ObjectId, pobrany z URL | ciąg “Country deleted” | 204 |
| /api/country/{id}/manufacturers | GET | getManufacturers | obiekt klasy ObjectId, pobrany z URL | lista identyfikatorów producentów | 200 |
| /api/country/{id}/manufacturers | POST | addManufacturers | obiekt klasy ObjectId, pobrany z URL;
lista identyfikatorów producentów | obiekt klasy Country | 201 |
| /api/country/{id}/manufacturers | DELETE | deleteManufacturers | obiekt klasy ObjectId, pobrany z URL;
lista identyfikatorów producentów | obiekt klasy Country | 204 |

### /api/manufacturer

| endpoint | metoda HTTP | nazwa funkcji | argumenty funkcji | zwrot funkcji | status HTTP |
| --- | --- | --- | --- | --- | --- |
| /api/manufacturer | POST | create | obiekt klasy Manufacturer | obiekt klasy Manufacturer | 201 |
| /api/manufacturer | GET | getAll | - | lista obiektów klasy Manufacturer | 200 |
| /api/manufacturer/{id} | GET | getById | obiekt klasy ObjectId, pobrany z URL | obiekt klasy Manufacturer | 200 |
| /api/manufacturer/{id} | DELETE | delete | obiekt klasy ObjectId, pobrany z URL | ciąg “Manufacturer deleted” | 204 |

### /api/category

| endpoint | metoda HTTP | nazwa funkcji | argumenty funkcji | zwrot funkcji | status HTTP |
| --- | --- | --- | --- | --- | --- |
| /api/category | POST | create | obiekt klasy Category | obiekt klasy Category | 201 |
| /api/category | GET | getAll | - | lista obiektów klasy CategoryResponse | 200 |
| /api/category/{id} | GET | getById | obiekt klasy ObjectId, pobrany z URL | obiekt klasy CategoryResponse | 200 |
| /api/category/{id} | DELETE | delete | obiekt klasy ObjectId, pobrany z URL | ciąg “Category deleted” | 204 |
| /api/category/{id}/subcategories | GET | getSubcategories | obiekt klasy ObjectId, pobrany z URL | lista identyfikatorów podkategorii | 200 |
| /api/category/{id}/subcategories | POST | addSubcategories | obiekt klasy ObjectId, pobrany z URL;
lista identyfikatorów podkategorii | obiekt klasy Category | 201 |
| /api/category/{id}/subcategories | DELETE | deleteSubcategories | obiekt klasy ObjectId, pobrany z URL;
lista identyfikatorów podkategorii | obiekt klasy Category | 204 |

### /api/subcategory

| endpoint | metoda HTTP | nazwa funkcji | argumenty funkcji | zwrot funkcji | status HTTP |
| --- | --- | --- | --- | --- | --- |
| /api/subcategory | POST | create | obiekt klasy Subcategory | obiekt klasy Subcategory | 201 |
| /api/subcategory | GET | getAll | - | lista obiektów klasy Subcategory | 200 |
| /api/subcategory/{id} | GET | getById | obiekt klasy ObjectId, pobrany z URL | obiekt klasy Subcategory | 200 |
| /api/subcategory/{id} | DELETE | delete | obiekt klasy ObjectId, pobrany z URL | ciąg “Subcategory deleted” | 204 |

### /api/product

| endpoint | metoda HTTP | nazwa funkcji | argumenty funkcji | zwrot funkcji | status HTTP |
| --- | --- | --- | --- | --- | --- |
| /api/product | POST | create | obiekt klasy Product | obiekt klasy Product | 201 |
| /api/product | GET | getAll | - | lista obiektów klasy ProductResponse | 200 |
| /api/product/{id} | GET | getById | obiekt klasy ObjectId, pobrany z URL | obiekt klasy ProductResponse | 200 |
| /api/product/{id} | DELETE | delete | obiekt klasy ObjectId, pobrany z URL | ciąg “Product deleted” | 204 |
| /api/product/{id}/subcategory | GET | addSubcategories | obiekt klasy ObjectId, pobrany z URL;
identyfikator podkategorii | obiekt klasy Product | 200 |
| /api/product/{id}/manufacturer | DELETE | addManufacturer | obiekt klasy ObjectId, pobrany z URL;
identyfikator producenta | obiekt klasy Product | 204 |

# Podsumowanie

## Rozwój

Przed aplikacją stoją następujące możliwości rozwoju:

- pełny system autoryzacji
- lepsza reprezentacja danych
- większa ilość specyficznych endpointów
- zmiana na relacyjną bazę danych, która ułatwiłaby zarządzanie relacjami
- rozszerzenie możliwości interfejsu webowego

## Zakończenie

Projekt pozwolił na stworzenie w pełni funkcjonalnego API, zgodnego ze standardami programistycznymi, które dodatkowo zapewnia walidację przesyłanych danych oraz obsługę błędów. Backend został zrealizowany w języku Java z wykorzystaniem frameworka Spring Boot, co znacząco ułatwiło implementację i zarządzanie logiką serwerową. Do przechowywania danych użyto nierelacyjnej bazy danych MongoDB.

Dodatkowo powstał interfejs użytkownika w formie aplikacji internetowej zbudowanej w frameworku NextJS, bazującym na języku JavaScript (TypeScript). Interfejs ten w pełni wykorzystuje funkcjonalności API, umożliwiając dostęp do kluczowych funkcji systemu w intuicyjny sposób.

Projekt połączył kluczowe technologie potrzebne do stworzenia aplikacji webowej, integrującej zarówno backend, jak i frontend. Wszystkie użyte komponenty zostały poprawnie zintegrowane, zapewniając stabilne i efektywne działanie aplikacji.