# Access Rights
Concise Kotlin DSL for Unix access rights

Usage
```kotlin
val mode = Mode { rwx + rx + rx }
mode.str  // "rwxr-xr-x"
mode.bits // 755 (octal)

```

```kotlin
Mode { rwx + rwx + rwx }    // 777, rwxrwxrwx
Mode { void + void + void } // 000, ---------
Mode { rw + r + r }         // 644, rw-r--r--
```

