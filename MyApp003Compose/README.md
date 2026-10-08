# MyApp003Compose – Hoď kostkou (Jetpack Compose)

Úkol 003, varianta 2: šablona *Empty Activity*, celé UI je definované v Kotlinu (Jetpack Compose).

- `Scaffold` předává `innerPadding`, takže obsah není pod systémovými lištami; vše je vystředěné v `Column`.
- Po stisknutí **Hodit** se kostka 10× náhodně změní s prodlevou 250 ms (korutina) a pak zobrazí výsledek; tlačítka jsou během animace zakázaná (`enabled = !isRolling`).

## Vlastní vylepšení

- **Statistika hodů** – kolikrát padla každá stěna ⚀–⚅, celkový počet hodů a průměrná hodnota.
- **Zvýrazněná šestka** – když padne 6, kostka zezlátne a zobrazí se „Šestka! 🎉“.
- Tlačítko **Vynulovat statistiku**.

## Jak se aktualizuje kostka

Deklarativně – hodnota kostky, stav animace i počty stěn jsou **stav** (`mutableIntStateOf`, `mutableStateListOf`). Kód mění jen tato data a Compose obrazovku podle nich sám překreslí (recomposition); na žádný prvek se ručně nesahá.
