# Fslang

FSlang is a lambda calculus interpreter written in java. 
It is a port of lazy lambda calulus interpreter written by L. Allison of Monash University  and 
Sinan https://github.com/sinsinan/fslang.
Full article about the same is available at https://users.monash.edu.au/~lloyd/tildeFP/Lambda


```
program ::= exp

exp ::= ident | numeral | 'letter' | () | true | false | nil |
        ( exp ) | unopr exp | exp binopr exp |
        if exp then exp else exp |
        lambda param . exp  |  exp exp |
        let [rec] decs in exp

decs  ::= dec , decs | dec
dec   ::= ident = exp

param ::= () | ident

unopr ::= hd | tl | null | not | -
binopr ::= and | or | = | <> | < | <= | > | >= | + | - | * | / | ::

priorities: ::               1 cons list (right associative)
            or               2
            and              3
            = <> < <= > >=   4 scalars only
            + -              5 (binary -)
            * /              6
            application      7 {left associative, f x y = (f(x))(y)}
            - hd tl null not 8 (unary -)

```
