# MyApp003Xml – Hoď kostkou (XML + findViewById)

Úkol 003, varianta 1: šablona *Empty Views Activity*, layout v XML, prvky získané přes `findViewById`.

- `LinearLayout` `llMain` s odsazením od systémových lišt, prvky `tvTitle`, `tvDice`, `tvResult`, `btnRoll`, `tvStats`, `tvSummary`, `btnReset`.
- Po stisknutí **Hodit** se kostka 10× náhodně změní s prodlevou 250 ms a pak zobrazí výsledek; tlačítka jsou během animace zakázaná.

## Vlastní vylepšení

- **Statistika hodů** – kolikrát padla každá stěna ⚀–⚅, celkový počet hodů a průměrná hodnota.
- **Zvýrazněná šestka** – když padne 6, kostka zezlátne a zobrazí se „Šestka! 🎉“.
- Tlačítko **Vynulovat statistiku**.

## Jak se aktualizuje kostka

Imperativně – kód sám najde `TextView` (`findViewById`) a při každé změně ručně přepíše jeho text a barvu (`tvDice.text = …`, `setTextColor(…)`). Stejně ručně se po hodu přepíše i statistika.
