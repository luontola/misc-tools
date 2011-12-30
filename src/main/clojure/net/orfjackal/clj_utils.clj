(ns net.orfjackal.clj-utils)

(defmacro def- [name& decls]
  (list* `def (with-meta name (assoc (meta name) :private true)) decls))

(def count-if (comp count filter))
