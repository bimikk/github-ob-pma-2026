# MyApp005ObjednavkaV2 – Objednávka bot V2

Úkol 005 – dvouobrazovková objednávka bot (Adidas, Nike, Hoka) v Kotlinu. Navazuje na úkol 004.

## Hlavní obrazovka (`MainActivity`)

- Rozvržení `ConstraintLayout`.
- `RadioGroup` – výběr právě jednoho modelu (mění se i obrázek boty).
- `CheckBox`y – nezávislá příplatková výbava: černá barva (+200 Kč), černé tkaničky (+90 Kč), obal na boty (+250 Kč).
- Celková cena se přepočítá při každé změně; události se zachytávají lambda výrazy.
- Tlačítko **Pokračovat k rekapitulaci** předá model, výbavu a cenu přes `Intent`.

## Rekapitulace (`SummaryActivity`)

- `ScrollView` s `android:fillViewport="true"`, uvnitř `ConstraintLayout`.
- Data se čtou z `Intentu` přes `getStringExtra` (model, cena) a `getBooleanExtra` (výbava) a skládají se do přehledu.
- Zvýrazněné pole s celkovou cenou, dlouhé obchodní podmínky pro ukázku scrollování.
- Tlačítko **Zpět k úpravě** ukotvené u spodní hrany volá `finish()` – výběr na první obrazovce zůstane zachovaný.

Propojení UI výhradně přes **ViewBinding** (žádné `findViewById`).

## Návazný úkol: jak objednávky trvale ukládat

| Možnost | Co to je | Kdy se hodí |
|---|---|---|
| **SharedPreferences** | jednoduché páry klíč–hodnota v XML souboru aplikace | uložit poslední objednávku nebo předvyplnit poslední výběr |
| **Jetpack DataStore** | moderní náhrada SharedPreferences (asynchronní, Kotlin Flow) | totéž co výše, doporučená varianta pro nové aplikace |
| **Room (SQLite)** | lokální databáze s tabulkou objednávek (`@Entity`, `@Dao`, `@Database`) | historie všech objednávek, filtrování a řazení – nejvhodnější pro tuto aplikaci |
| **Soubor (JSON)** | objednávky serializované do souboru v interním úložišti | jednoduchý export / záloha bez databáze |
| **Cloud (Firebase Firestore, vlastní REST API)** | objednávky uložené na serveru | sdílení mezi zařízeními, zpracování objednávek obchodem |

Pro tuto aplikaci by dávalo smysl **Room**: každá potvrzená objednávka by se uložila jako řádek (model, výbava, cena, datum) a šla by zobrazit v historii objednávek.
