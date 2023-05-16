# fslang

#  Brief Intro to Lambda Calculus with python

what if function are anly allowed in this universe ,  specifically single paramter functions 


```python
def f(x):
    return x 

def f(x):
    return x(x) 

def f(x):
    def g(y):
        return x(y)
    return g

```

## how to model swtich 


```python
def LEFT(a):
    def f(b):
        return a
    return f

def RIGHT(a):
    def f(b):
        return b
    return f
```


```python
LEFT('5v')('gnd')
```




    '5v'




```python
RIGHT('5v')('gnd')
```




    'gnd'



this a behaviour, not the values 

 Booleans


```python
TRUE = lambda x : lambda y : x
```


```python
FALSE = lambda x : lambda y : y
```


```python
TRUE('5v')('gnd')
```




    '5v'




```python
FALSE('5v')('gnd')
```




    'gnd'



## Boolean Operators


```python
def NOT(x):
    return x(FALSE)(TRUE)
```


```python
assert NOT(TRUE) is FALSE 
assert NOT(FALSE) is TRUE
```


```python
NOT(TRUE)
```




    <function __main__.FALSE(x)>




```python
NOT(FALSE)
```




    <function __main__.TRUE(x)>




```python
def AND(x):
    return lambda y : x(y)(x)


```


```python
AND(TRUE)(TRUE)
```




    <function __main__.TRUE(x)>




```python
AND(TRUE)(FALSE)
```




    <function __main__.FALSE(x)>




```python
AND(FALSE)(TRUE)
```




    <function __main__.FALSE(x)>




```python
AND(FALSE)(FALSE)
```




    <function __main__.FALSE(x)>




```python
True or False 
```




    True




```python
False or True
```




    True




```python


def OR(x):
    return lambda y : x(x)(y)
```


```python
OR(TRUE)(TRUE)
```




    <function __main__.TRUE(x)>




```python
OR(TRUE)(FALSE)
```




    <function __main__.TRUE(x)>




```python
OR(FALSE)(TRUE)
```




    <function __main__.TRUE(x)>




```python
OR(FALSE)(FALSE)
```




    <function __main__.FALSE(x)>



# Number | Church Numerals 


```python

ZERO = lambda f : lambda x : x
ONE  = lambda f : lambda x : f(x)
TWO  = lambda f : lambda x : f(f(x))
THREE  = lambda f : lambda x : f(f(f(x)))
FOUR  = lambda f : lambda x : f(f(f(f(x))))

```


```python
f = lambda  x:  '*' + x
```


```python
THREE(f)("")
```




    '***'




```python
incr = lambda x : x + 1
```


```python
THREE(FOUR)
```




    <function __main__.<lambda>.<locals>.<lambda>(x)>




```python
THREE(FOUR)(incr)(0)
```




    64




```python
4**3
```




    64



Implementation of ZERO and FALSE are the same in lambda calculus Why ?


```python

SUCC = lambda n : lambda f : lambda x: f(n(f)(x))
```


```python
SUCC(TWO)
```




    <function __main__.<lambda>.<locals>.<lambda>(f)>




```python
_(incr)(0)
```




    3




```python
ADD = lambda x : lambda y : y(SUCC)(x)
```


```python
ADD(FOUR)(THREE)
```




    <function __main__.<lambda>.<locals>.<lambda>(f)>




```python
_(incr)(0)
```




    7




```python
ADD(THREE)(THREE)
```




    <function __main__.<lambda>.<locals>.<lambda>(f)>




```python
_(incr)(0)
```




    6




```python
MUL = lambda x : lambda y : lambda f : y(x(f))
```


```python
MUL(THREE)(FOUR)
```




    <function __main__.<lambda>.<locals>.<lambda>.<locals>.<lambda>(f)>




```python
_(incr)(0)
```




    12



# α Conversion

- can rename an argument of the funciton
- can't introduce a clash 

AND = λxy.xyx

AND = λzy.zyz




# β  Reduction

- can substitute arguments

λxy.xyx(ab)  =>  λy.( ab )y( ab )


---
- can't introduce name clashes 

λxy.xyx(xy)

```
x ,y = 3 , 5

def f(x,y):
   return 2*x + y
   
```

# Data Struct


```python
CONS = lambda a : lambda b : lambda s : s(a)(b)
```


```python
p = CONS(THREE)(TWO)
```


```python
CAR = lambda p : p(TRUE)
CDR = lambda p : p(FALSE)
```


```python
CAR(p)
```




    <function __main__.<lambda>(f)>




```python
_(incr)(0)
```




    3




```python
CDR(p)
```




    <function __main__.<lambda>(f)>




```python
_(incr)(0)
```




    2




```python
## how to implement predecssor 
```


```python
#(0,0)
#(1,0)
#(2,1)
#(2,2)
#(3,3)

def t(p):
    return (p[0]+1, p[0])
```


```python
THREE(t)((0,0))
```




    (3, 2)




```python
T = lambda p : CONS(SUCC(CAR(p)))(CAR(p))
```


```python
FOUR(T)(CONS(ZERO)(ZERO))
```




    <function __main__.<lambda>.<locals>.<lambda>.<locals>.<lambda>(s)>




```python
a =_
```


```python
CAR(a)(incr)(0)
```




    4




```python
CDR(a)(incr)(0)
```




    3




```python

PRED = lambda n : CDR(n(T)(CONS(ZERO)(ZERO)))
```


```python
FOUR 
```




    <function __main__.<lambda>(f)>




```python
PRED(FOUR)
```




    <function __main__.<lambda>.<locals>.<lambda>(f)>




```python
_(incr)(0)
```




    3




```python

```

## Other Functions


```python
ZERO = lambda f : lambda x : x
TWO  = lambda f : lambda x : f(f(x))
```


```python
ISZERO = lambda n : n(lambda g : FALSE)(TRUE)
```


```python
TRUE = lambda true : lambda x : true
FALSE = lambda false : lambda x : x

# bit hack to understand whats happening 
```


```python
ZERO(lambda g :  FALSE)(TRUE)
```




    <function __main__.<lambda>(true)>




```python
TWO(lambda g : FALSE )(TRUE)
```




    <function __main__.<lambda>(false)>




```python
(lambda g : FALSE )(TRUE)(lambda g : FALSE )(TRUE)
```




    <function __main__.<lambda>(true)>



### factorial


```python
FACT = lambda n: ISZERO(n) \
                (ONE) \
                (MUL(n)(FACT(PRED(n))))
```


```python
FACT(THREE)
```


    ---------------------------------------------------------------------------

    RecursionError                            Traceback (most recent call last)

    Cell In[283], line 1
    ----> 1 FACT(THREE)


    Cell In[280], line 3, in <lambda>(n)
          1 FACT = lambda n: ISZERO_LAZY(n) \
          2                 (ONE) \
    ----> 3                 (MUL(n)(FACT(PRED(n))))


    Cell In[280], line 3, in <lambda>(n)
          1 FACT = lambda n: ISZERO_LAZY(n) \
          2                 (ONE) \
    ----> 3                 (MUL(n)(FACT(PRED(n))))


        [... skipping similar frames: <lambda> at line 3 (2967 times)]


    Cell In[280], line 3, in <lambda>(n)
          1 FACT = lambda n: ISZERO_LAZY(n) \
          2                 (ONE) \
    ----> 3                 (MUL(n)(FACT(PRED(n))))


    Cell In[269], line 1, in <lambda>(n)
    ----> 1 PRED = lambda n : CDR(n(T)(CONS(ZERO)(ZERO)))


    Cell In[174], line 2, in <lambda>(p)
          1 CAR = lambda p : p(TRUE)
    ----> 2 CDR = lambda p : p(FALSE)


    Cell In[164], line 1, in <lambda>(s)
    ----> 1 CONS = lambda a : lambda b : lambda s : s(a)(b)


    RecursionError: maximum recursion depth exceeded



```python
TRUE_LAZY   = lambda x : lambda y : x()
FALSE_LAZY  = lambda x : lambda y : y()
ISZERO_LAZY = lambda n : n(lambda g : FALSE_LAZY)(TRUE_LAZY)
```


```python
FACT = lambda n: ISZERO_LAZY(n) \
                (lambda: ONE) \
                (lambda: MUL(n)(FACT(PRED(n))))
```


```python
FACT(THREE)
```




    <function __main__.<lambda>.<locals>.<lambda>.<locals>.<lambda>(f)>




```python
_(incr)(0)
```




    6




```python

```
