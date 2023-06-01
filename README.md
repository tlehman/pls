# pls

A Clojure library for working with [PLS](https://en.wikipedia.org/wiki/PLS_(file_format)) files.

## Usage

```clojure
(pls-parse-file "/home/tlehman/foo.pls")

;; => 

({:file "http://prem1.di.fm:80/drumandbass", :title "DI.FM - Drum and Bass"}
 {:file "http://prem4.di.fm:80/liquiddubstep", :title "DI.FM - Liquid Dubstep"})
;; ...
```

## License

Copyright © 2023 Tobi Lehman

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
http://www.eclipse.org/legal/epl-2.0.

This Source Code may also be made available under the following Secondary
Licenses when the conditions for such availability set forth in the Eclipse
Public License, v. 2.0 are satisfied: GNU General Public License as published by
the Free Software Foundation, either version 2 of the License, or (at your
option) any later version, with the GNU Classpath Exception which is available
at https://www.gnu.org/software/classpath/license.html.
