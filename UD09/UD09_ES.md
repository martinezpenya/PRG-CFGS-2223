---
title: UD09: Interfaz gráfica
language: ES
author: David Martínez Peña [www.martinezpenya.es]
subject: Programación
keywords: [PRG, 2022, Programacion, Java]
IES: IES Eduardo Primo Marqués (Carlet) [www.ieseduardoprimo.es]
header: ${title} - ${subject} (ver. ${today}) 
footer:${currentFileName}.pdf - ${author} - ${IES} - ${pageNo}/${pageCount}
typora-root-url:${filename}/../
typora-copy-images-to:${filename}/../assets
---
[toc]

# Introducción

El proyecto de documentación de JavaFX tiene como objetivo recopilar información útil para los desarrolladores de JavaFX de toda la web. El proyecto es [de código abierto](https://translate.google.com/website?sl=en&amp;tl=es&amp;hl=ca&amp;client=webapp&amp;u=http://www.github.com/FXDocs/docs) y fomenta la participación de la comunidad para garantizar que la documentación sea lo más pulida y útil posible.

# Gráfico de escena

## Descripción general

Un gráfico de escena es una estructura de datos de árbol que organiza (y agrupa) objetos gráficos para una representación lógica más sencilla. También permite que el motor de gráficos represente los objetos de la manera más eficiente al omitir total o parcialmente los objetos que no se verán en la imagen final. La siguiente figura muestra un ejemplo de la arquitectura del gráfico de escena JavaFX.

![gráfico de escena](/assets/scene_graph.jpg)

En la parte superior de la arquitectura hay un `Stage`. Una etapa es una representación JavaFX de una ventana de sistema operativo nativo. En un momento dado, un escenario puede tener un solo `Scene`adjunto. Una escena es un contenedor para el gráfico de escena JavaFX.

Todos los elementos en el gráfico de escena JavaFX se representan como `Node`objetos. Hay tres tipos de nudos: raíz, rama y hoja. El nodo raíz es el único nodo que no tiene un padre y está contenido directamente en una escena, que se puede ver en la figura anterior. La diferencia entre una rama y una hoja es que un nodo hoja no tiene hijos.

En el gráfico de escena, los nodos secundarios comparten muchas propiedades de un nodo principal. Por ejemplo, una transformación o un evento aplicado a un nodo padre también se aplicará recursivamente a sus hijos. Como tal, una jerarquía compleja de nodos se puede ver como un solo nodo para simplificar el modelo de programación. Exploraremos transformaciones y eventos en secciones posteriores.

En la siguiente figura se puede ver un ejemplo de un gráfico de escena "Hola Mundo".

![gráfico de escena específico](/assets/specific_scene_graph.jpg)

Una posible implementación que producirá un gráfico de escena que coincida con la figura anterior es la siguiente.

**`E01_HolaMundo.java`**

```java
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class HolaMundo extends Application {

    private Parent createContent() {
        return new StackPane(new Text("Hola Mundo"));
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setScene(new Scene(createContent(), 400, 400));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
```

El resultado de ejecutar el código se ve en la siguiente figura.

<img src="/assets/HolaMundo.png" style="zoom:67%;" />

Notas importantes:

- Un nodo puede tener un máximo de 1 padre.
- Un nodo en el gráfico de escena "activo" (adjunto a una escena actualmente visible) solo se puede modificar desde el subproceso de la aplicación JavaFX.

## Transformaciones

Usaremos la siguiente aplicación como ejemplo para demostrar las 3 transformaciones más comunes.

**`E02_TransformApp.java`**

```java
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class E02_TransformApp extends Application {

    private Parent createContent() {
        Rectangle box = new Rectangle(100, 50, Color.BLUE);
        transform(box);
        return new Pane(box);
    }

    private void transform(Rectangle box) {
        // we will apply transformations here:

        //Uncomment for translate
        box.setTranslateX(100);
        box.setTranslateY(200);

        //uncomment for scale
        box.setScaleX(1.5);
        box.setScaleY(1.5);

        //uncomment for rotate
        box.setRotate(30);
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setScene(new Scene(createContent(), 300, 300, Color.GRAY));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
```

Ejecutar la aplicación dará como resultado la siguiente imagen.

![](/assets/bluebox.png)

En JavaFX, puede ocurrir una transformación simple en uno de los 3 ejes:  X, Y o Z. La aplicación de ejemplo está en 2D, por lo que solo consideraremos los ejes X e Y.

### Translación

En JavaFX y gráficos por computadora, `translate`significa moverse. Podemos trasladar nuestra caja en 100 píxeles en el eje X y 200 píxeles en el eje Y.

```java
private void transform(Rectangle box) {
    box.setTranslateX(100);
    box.setTranslateY(200);
}
```

### Escala

Puede aplicar la escala para hacer un nodo más grande o más pequeño. El valor de escala es una relación. Por defecto, un nodo tiene un valor de escala de 1 (100%) en cada eje. Podemos agrandar nuestra caja aplicando una escala de 1.5 en los ejes X e Y.

```java
private void transform(Rectangle box) {
    box.setScaleX(1.5);
    box.setScaleY(1.5);
}
```

### Rotación

La rotación de un nodo determina el ángulo en el que se representa el nodo. En 2D el único eje de rotación sensible es el eje Z. Giremos la caja 30 grados.

```java
private void transform(Rectangle box) {
    box.setRotate(30);
}
```

## Manejo de eventos

Un evento notifica que ha ocurrido algo importante. Los eventos suelen ser lo "primitivo" de un sistema de eventos (también conocido como bus de eventos). Generalmente, un sistema de eventos tiene las siguientes 3 responsabilidades:

- `fire` (desencadenar) un evento,
- notificar `listeners` (a las partes interesadas) sobre el evento y
- `handle` (procesar) el evento.

El mecanismo de notificación de eventos lo realiza la plataforma JavaFX automáticamente. Por lo tanto, solo consideraremos cómo disparar eventos, escuchar eventos y cómo manejarlos.

Primero, vamos a crear un evento personalizado.

**`E03_EventoUsuario.java`**

```java
import javafx.event.Event;
import javafx.event.EventType;

public class E03_EventoUsuario extends Event {

    public static final EventType<E03_EventoUsuario> ANY = new EventType<>(Event.ANY, "ANY");

    public static final EventType<E03_EventoUsuario> LOGIN_SUCCEEDED = new EventType<>(ANY, "LOGIN_SUCCEEDED");

    public static final EventType<E03_EventoUsuario> LOGIN_FAILED = new EventType<>(ANY, "LOGIN_FAILED");

    public E03_EventoUsuario(EventType<? extends Event> eventType) {
        super(eventType);
    }
    // cualquier otro atributo importante como la fecha, la hora...
}
```

Dado que los tipos de eventos son fijos, generalmente se crean dentro del mismo archivo de origen que el evento. Podemos ver que hay 2 tipos específicos de eventos: `LOGIN_SUCCEEDED`y `LOGIN_FAILED`. Podemos escuchar estos tipos específicos de eventos:

```java
Node node = ...
node.addEventHandler(UserEvent.LOGIN_SUCCEEDED, event -> {
    // handle event
});
```

Alternativamente, podemos manejar cualquier `UserEvent`:

```java
Node node = ...
node.addEventHandler(UserEvent.ANY, event -> {
    // handle event
});
```

Finalmente, podemos construir y disparar nuestros propios eventos:

```java
UserEvent event = new UserEvent(UserEvent.LOGIN_SUCCEEDED);
Node node = ...
node.fireEvent(event);
```

Por ejemplo, `LOGIN_SUCCEEDED`o `LOGIN_FAILED` podría activarse cuando un usuario intenta iniciar sesión en una aplicación. Según el resultado del inicio de sesión, podemos permitir que el usuario acceda a la aplicación o bloquearlo. Si bien se puede lograr la misma funcionalidad con una `if` declaración simple, hay una ventaja significativa de un sistema de eventos. Los sistemas de eventos se diseñaron para permitir la comunicación entre varios módulos (subsistemas) en una aplicación sin acoplarlos estrechamente. Como tal,  un sistema de audio puede reproducir un sonido cuando el usuario inicia sesión. Por lo tanto, mantiene todo el código relacionado con el audio en su propio módulo. Sin embargo, no profundizaremos en los estilos arquitectónicos.

### Eventos de entrada

Los eventos de teclado y ratón son los tipos de eventos más comunes utilizados en JavaFX. Cada `Node ` proporciona los llamados "métodos de conveniencia" para manejar estos eventos. Por ejemplo, podemos ejecutar algún código cuando se presiona un botón:

```java
Button button = ...
button.setOnAction(event -> {
    // button was pressed
});
```

Para mayor flexibilidad también podemos usar lo siguiente:

```java
Button button = ...
button.setOnMouseEntered(e -> ...);
button.setOnMouseExited(e -> ...);
button.setOnMousePressed(e -> ...);
button.setOnMouseReleased(e -> ...);
```

El objeto `e` anterior es de tipo `MouseEvent` y se puede consultar para obtener información diversa sobre el evento, por ejemplo, `x` posiciones `y`, número de clics, etc. Finalmente, podemos hacer lo mismo con las teclas:

```java
Button button = ...
button.setOnKeyPressed(e -> ...);
button.setOnKeyReleased(e -> ...);
```

El objeto `e`aquí es de tipo `KeyEvent` y lleva información sobre el código de la tecla, que luego se puede asignar a una tecla física real en el teclado.

## Sincronización

Es importante comprender la diferencia de tiempo entre la creación de controles de interfaz de usuario de JavaFX y la visualización de los controles. Al crear los controles de la interfaz de usuario, ya sea a través de la creación directa de objetos API o mediante FXML, es posible que te falten ciertos valores de geometría de pantalla, como las dimensiones de una ventana. Eso está disponible más tarde, en el instante en que se muestra la pantalla al usuario. Ese evento de visualización, llamado OnShown, es el momento en que se ha asignado una ventana y se completan los cálculos de diseño final.

Para demostrar esto, considere el siguiente programa que muestra las dimensiones de la pantalla mientras se crean los controles de la interfaz de usuario y las dimensiones de la pantalla cuando se muestra la pantalla. La siguiente captura de pantalla muestra la ejecución del programa. Cuando se crean los controles de la interfaz de usuario (`new VBox()`, `new Scene()`, `primaryStage.setScene()`), no hay valores reales de alto y ancho de ventana disponibles como lo demuestran los valores "NaN"  indefinidos.

![](/assets/strartvsshow.png)

Sin embargo, los valores de ancho y alto están disponibles una vez que se muestra la ventana. El programa registra un controlador de eventos para el evento `OnShown` y prepara la misma salida.

La siguiente es la clase Java del programa de demostración.

**`E04_StartVsShown.java`**

```java
public class StartVsShownJavaFXApp extends Application {

    private DoubleProperty startX = new SimpleDoubleProperty();
    private DoubleProperty startY = new SimpleDoubleProperty();
    private DoubleProperty shownX = new SimpleDoubleProperty();
    private DoubleProperty shownY = new SimpleDoubleProperty();

    @Override
    public void start(Stage primaryStage) throws Exception {

        Label startLabel = new Label("Start Dimensions");
        TextField startTF = new TextField();
        startTF.textProperty().bind(
                Bindings.format("(%.1f, %.1f)", startX, startY)
        );

        Label shownLabel = new Label("Shown Dimensions");
        TextField shownTF = new TextField();
        shownTF.textProperty().bind(
                Bindings.format("(%.1f, %.1f)", shownX, shownY)
        );

        GridPane gp = new GridPane();
        gp.add( startLabel, 0, 0 );
        gp.add( startTF, 1, 0 );
        gp.add( shownLabel, 0, 1 );
        gp.add( shownTF, 1, 1 );
        gp.setHgap(10);
        gp.setVgap(10);

        HBox hbox = new HBox(gp);
        hbox.setAlignment(CENTER);

        VBox vbox = new VBox(hbox);
        vbox.setAlignment(CENTER);

        Scene scene = new Scene( vbox, 480, 320 );

        primaryStage.setScene( scene );

        // before show()...I just set this to 480x320, right?
        startX.set( primaryStage.getWidth() );
        startY.set( primaryStage.getHeight() );

        primaryStage.setOnShown( (evt) -> {
            shownX.set( primaryStage.getWidth() );
            shownY.set( primaryStage.getHeight() );  // all available now
        });

        primaryStage.setTitle("Start Vs. Shown");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
```

A veces, conocerá las dimensiones de la pantalla de antemano y puede usar esos valores en cualquier punto del programa JavaFX. Esto incluye antes del evento `OnShown`. Sin embargo, si su secuencia de inicialización contiene lógica que necesita estos valores, deberá trabajar con el evento `OnShown`. Un caso de uso podría ser trabajar con las últimas dimensiones guardadas o dimensiones basadas en la entrada del programa.

# Controles de la interfaz de usuario

## `Label`

La clase `Label` que reside en el paquete `javafx.scene.control` de la API de JavaFX se puede usar para mostrar un elemento de texto.

La etiqueta de la izquierda es un elemento de texto de color azul, que se auto-ajusta si cambiamos el tamaño de la ventana, la etiqueta del centro representa texto girado y la etiqueta de la derecha representa un texto con imagen, que además aumenta su tamaño cuando pasamos por encima.

![Label](/assets/label.png)

La API de JavaFX proporciona tres constructores de la clase `Label` para crear etiquetas en su aplicación.


```java
//Creamos la etiqueta vacia
Label label1 = new Label();

//Creamos la etiqueta con texto
Label label2 = new Label("Etiqueta2");

//Creamos la etiqueta con imágen
Image image = new Image("UD09/label.png");
Label label3 = new Label("Search", new ImageView(image));
```

Una vez que haya creado una etiqueta, puede cambiar sus propiedades con los métodos;

- `setText(String text)` especifica el texto para la etiqueta.
- `setGraphic(Node graphic)`: especifica el icono gráfico,
- `setTextFill` especifica el color para pintar el elemento de texto de la etiqueta.
- `setGraphicTextGap` para establecer el espacio entre ellos.
- `setWrapText` para indicar si debe autoajustarse (`true`) o no (`false`)
- `setTextAlignment` puede variar la posición del contenido de la etiqueta dentro de su área de diseño
- `setContentDisplay` puede definir la posición del gráfico en relación con el texto aplicando el método  y especificando una de las siguientes constantes `ContentDisplay`: `LEFT`, `RIGHT`, `CENTER`, `TOP`, `BOTTOM` .

Cuando se activa el evento `MOUSE_ENTERED` en la etiqueta, se establece el factor de escala de 1,5 para los métodos `setScaleX` y `setScaleY`. Cuando un usuario mueve el cursor del ratón fuera de la etiqueta y ocurre el evento `MOUSE_EXITED`, el factor de escala se establece en 1.0 y la etiqueta se representa en su tamaño original.


```java
label3.setOnMouseEntered((MouseEvent event) -> {
    label3.setScaleX(1.5);
    label3.setScaleY(1.5);
});

label3.setOnMouseExited((MouseEvent event) -> {
    label3.setScaleX(1);
    label3.setScaleY(1);
});
```

## `Button`

La clase `Button` disponible a través de la API de JavaFX permite a los desarrolladores procesar una acción cuando un usuario hace clic en un botón. La clase `Button` es una extensión de la clase `Labeled`. Puede mostrar texto, una imagen o ambos.

![Button](/button.png)

Puede crear un control `Button` en una aplicación JavaFX usando tres constructores de la clase Button como se muestra a continuación


```java
//Creamos el botón vacio
Button button1 = new Button();
//Creamos el botón con texto
Button button2 = new Button("Sí");
//Creamos el botón con texto e imágen
Image image = new Image("UD09/ok.png");
Button button4 = new Button("Aceptar", new ImageView(image));
```

La clase `Button`  puede usar los siguientes métodos:

- `setText(String text)`: especifica el título de texto para el botón
- `setGraphic(Node graphic)`: especifica el icono gráfico
- `setOnAction`: La función principal de cada botón es producir una acción cuando se hace clic en él. En nuestro ejemplo cambiar el texto de un label.


```
button2.setOnAction((ActionEvent e) -> {
    label.setText("Aceptado");
});
```

Vemos cómo procesar un `ActionEvent`, de modo que cuando un usuario presiona button2 el título de texto de la etiqueta label se establece en "Aceptado".

Puede usar la clase `Button` para establecer tantos métodos de manejo de eventos como necesite para causar el comportamiento específico o aplicar efectos visuales.

Debido a que la clase `Button` amplía la clase `Node`, puede aplicar cualquiera de los efectos del paquete `javafx.scene.effect` para mejorar la apariencia visual del botón. En este caso añadimos una sobra al botón con imagen cuando pasamos el ratón sobre el.


```java
//Creamos el estilo de sombra
DropShadow shadow = new DropShadow();
//Añadimos la sombra cuando pasamos sobre el botón
button4.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent e) -> {
    button4.setEffect(shadow);
});

//Eliminamos la sombra al salir del botón
button4.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent e) -> {
    button4.setEffect(null);
});
```

El siguiente paso para mejorar la apariencia visual de un botón es aplicar estilos CSS definidos por la clase `Skin`. Usar CSS en aplicaciones JavaFX 2 es similar a usar CSS en HTML, porque cada caso se basa en la misma especificación de CSS.

Puede definir estilos en un archivo CSS separado y habilitarlos en la aplicación usando el método `getStyleClass`. 

El fichero style.css:

```css
.button1{
    -fx-font: 22 ubuntu; 
    -fx-base: #b6e7c9;    
}
```

La propiedad `-fx-font` establece el nombre y el tamaño de la fuente para el button1. La propiedad `-fx-base` anula el color predeterminado aplicado al botón.

Y desde java usamos:


```java
//Asociamos el fichero css a nuestra escena
scene.getStylesheets().add("UD09/style.css");
[...]
//establecemos la clase correspondiente del css
button1.getStyleClass().add("button1");
```

 Como resultado, el button1 es de color verde claro con un tamaño de texto mayor.

## `RadioButton`

Un control `RadioButton` se puede seleccionar o deseleccionar. Por lo general, se combinan en un grupo donde solo se puede seleccionar un botón a la vez. 

Abajo se muestran tres capturas de pantalla del ejemplo `RadioButton`, en las que se agregan tres botones de opción a un grupo.

![En coche](/assets/coche.png)

![En moto](/assets/moto.png)

![A pie](/assets/pie.png)

La clase `RadioButton` disponible en el paquete `javafx.scene.control` del SDK de JavaFX proporciona dos constructores con los que puede crear un botón de opción. 


```java
//Creamos el botón vacio
RadioButton rButton1 = new RadioButton();
//añadimos texto una vez creado
rButton1.setText("Coche");
//Creamos los RadioButton con texto
RadioButton rButton2 = new RadioButton("Moto");
```

Puede seleccionar explícitamente un botón de radio utilizando el método `setSelected` y especificando su valor como `true`. Si necesita verificar si un usuario seleccionó un botón de radio en particular, aplique el método `isSelected`.

Debido a que la clase `RadioButton` es una extensión de la clase `Labeled`, puede especificar no solo una leyenda de texto, sino también una imagen. Utilice el método `setGraphic` para especificar una imagen.


```java
//Añadimos las imágenes a los Radio Button
ImageView imageCoche = new ImageView("UD09/coche.png");
rButton1.setGraphic(imageCoche);
```

Los `RadioButton` se utilizan normalmente en un grupo para presentar varias opciones mutuamente excluyentes. El objeto `ToggleGroup` proporciona referencias a todos los botones de radio asociados con él y los administra para que solo se pueda seleccionar uno de los botones de radio a la vez. A continuación se muestra como se crea un grupo de alternancia y especifica qué botón debe seleccionarse cuando se inicia la aplicación.


```java
//Creamos el grupo de alternancia
final ToggleGroup grupo = new ToggleGroup();
rButton1.setToggleGroup(grupo);
rButton1.setSelected(true); //si queremos que la primera opción este marcada por defecto
rButton2.setToggleGroup(grupo);
rButton3.setToggleGroup(grupo);
```

Normalmente, la aplicación realiza una acción cuando se selecciona uno de los botones de opción del grupo. En este caso cambiará la imágen que acompaña al grupo de alternancia.


```java
//Añadimos una imágen que cambiara al cambiar la selección
ImageView image = new ImageView();
grid.add(image, 1, 0, 1, 3);

//añadimos el listener al grupo para que capture el evento cuando se cambie la selección
grupo.selectedToggleProperty().addListener(
        (ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) -> {
            if (grupo.getSelectedToggle() != null) {
                image.setImage(new Image("UD09/" + grupo.getSelectedToggle().getUserData().toString() + ".png"));
            }
        });
```

Los datos de usuario se asignaron para cada botón de opción. El objeto `ChangeListener<Toggle>` verifica un conmutador seleccionado en el grupo. Utiliza el método `getSelectedToggle` para identificar qué botón de opción está actualmente seleccionado y extrae sus datos de usuario llamando al método `getUserData`. Luego, los datos del usuario se aplican para construir un nombre de archivo de imagen para cargar.

Por ejemplo, cuando se selecciona `rButton3`, el método `getSelectedToggle` devuelve "rButton3" y el método `getUserData` devuelve "coche" Por lo tanto, la imágen será "UD09/coche.png".

## `CheckBox`

Aunque las casillas de verificación se parecen a los `RadioButton`, no se pueden combinar en grupos de alternancia.

Más abajo se muestra una captura de pantalla de una aplicación en la que se usan tres casillas de verificación para habilitar o desactivar iconos en la barra de herramientas de una aplicación.

A continuación se crean tres casillas de verificación simples.


```java
//Creamos el CheckBox vacio
CheckBox check1 = new CheckBox();
//añadimos texto una vez creado
check1.setText("Coche");

//Creamos los CheckBox con texto
CheckBox check2 = new CheckBox("Moto");

//Hademos aparezca marcado por defecto
CheckBox check3 = new CheckBox("A pie");
check3.setSelected(true);
```

Una vez que haya creado una casilla de verificación, puede modificarla utilizando los métodos disponibles a través de las API de JavaFX. Por ejemplo el método `setText` define el título de texto de la casilla de verificación `check1`. El método `setSelected` se establece en `true` para que la casilla de verificación `check3` se seleccione cuando se inicie la aplicación.

La casilla de verificación puede estar definida o indefinida. Cuando está definido, puede seleccionarlo o deseleccionarlo. Sin embargo, cuando la casilla de verificación está indefinida, no se puede seleccionar ni deseleccionar. Se usa una combinación de los métodos `setSelected` y `setIndeterminate` de la clase `CheckBox` para especificar el estado de la casilla de verificación. En la tabla se muestran tres estados de una casilla de verificación en función de sus propiedades `INDETERMINADO` y `SELECCIONADO `.

| Valores de propiedad | Apariencia de la casilla de verificación |
| ---------------------------------------------- | -------------------------------------------------- ---------- |
| `INDETERMINADO` = **falso** `SELECCIONADO` = **falso** | ![La casilla de verificación no está seleccionada.](https://docs.oracle.com/javase/8/javafx/user-interface-tutorial/img/checkbox-state1.png) |
| `INDETERMINADO` =**falso** `SELECCIONADO` = **verdadero** | ![La casilla de verificación está seleccionada.](https://docs.oracle.com/javase/8/javafx/user-interface-tutorial/img/checkbox-state3.png) |
| `INDETERMINADO` = **verdadero** `SELECCIONADO` = **verdadero/falso** | ![La casilla de verificación no está definida.](https://docs.oracle.com/javase/8/javafx/user-interface-tutorial/img/checkbox-state2.png) |

Es posible que deba habilitar tres estados para las casillas de verificación en su aplicación cuando representan elementos de la interfaz de usuario que pueden estar en estados mixtos, por ejemplo, "Sí", "No", "No aplicable". La propiedad `allowIndeterminate` del objeto `CheckBox` determina si la casilla de verificación debe pasar por los tres estados: seleccionada, deseleccionada e indefinida. Si la variable es "verdadera", el control recorrerá los tres estados. Si es `falso`, el control recorrerá los estados seleccionado y deseleccionado. La aplicación descrita en la siguiente sección construye tres casillas de verificación y habilita solo dos estados para ellas.


```
final String[] names = new String[]{"Security", "Project", "Chart"};
final Image[] images = new Image[names.length];
final ImageView[] icons = new ImageView[names.length];
final CheckBox[] cbs = new CheckBox[names.length];

for (int i = 0; i < names.length; i++) {
    final Image image = images[i] =
        new Image(getClass().getResourceAsStream(names[i] + ".png"));
    final ImageView icon = icons[i] = new ImageView();
    final CheckBox cb = cbs[i] = new CheckBox(names[i]);
    cb.selectedProperty().addListener(
        (ObservableValue<? extends Boolean> ov,
            Boolean old_val, Boolean new_val) -> {
                icon.setImage(new_val ? image : null);                
    });
}
```

La matriz `names` utiliza un bucle `for` para crear una matriz de casillas de verificación y una matriz correspondiente de iconos. Por ejemplo, cbs[0], la primera casilla de verificación, tiene asignada la "Seguridad" subtítulo de texto Al mismo tiempo, la imagen[0] recibe "Security.png" como nombre de archivo para el método `getResourceStream` cuando se crea una imagen para el primer icono. Si se selecciona una casilla de verificación en particular, la imagen correspondiente se asigna al icono. Si se anula la selección de una casilla de verificación, el icono recibe una imagen "nula" y el icono no se representa.

[Figura 6-2](https://docs.oracle.com/javase/8/javafx/user-interface-tutorial/checkbox.htm#CHDJJAGF) muestra una aplicación cuando las casillas de verificación Seguridad y Gráfico están seleccionadas y la casilla de verificación Proyecto está deseleccionado.



Figura 6-2 Aplicación de casilla de verificación en acción

![Se seleccionan dos casillas de verificación](https://docs.oracle.com/javase/8/javafx/user-interface-tutorial/img/checkboxes-two.png)
[Descripción de "Figura 6-2 Aplicación Checkbox en acción"](https://docs.oracle.com/javase/8/javafx/user-interface-tutorial/img_text/checkboxes-two.htm)

## `TextBox`

En este capítulo se describen las capacidades del control de campo de texto.

La clase `TextField` implementa un control de interfaz de usuario que acepta y muestra la entrada de texto. Proporciona capacidades para recibir entradas de texto de un usuario. Junto con otro control de entrada de texto, `PasswordField`, esta clase amplía la clase `TextInput`, una superclase para todos los controles de texto disponibles a través de la API de JavaFX.

[Figura 8-1](https://docs.oracle.com/javase/8/javafx/user-interface-tutorial/text-field.htm#BABICHHH) muestra un campo de texto típico con una etiqueta.



Figura 8-1 Etiqueta y campo de texto

![Una etiqueta y un cuadro de texto](https://docs.oracle.com/javase/8/javafx/user-interface-tutorial/img/text-field-single.png)
[Descripción de "Figura 8-1 Etiqueta y campo de texto"](https://docs.oracle.com/javase/8/javafx/user-interface-tutorial/img_text/text-field-single.htm)







## Crear un campo de texto

En [Ejemplo 8-1](https://docs.oracle.com/javase/8/javafx/user-interface-tutorial/text-field.htm#BABHCDJJ), se usa un campo de texto en combinación con una etiqueta para indicar el tipo de contenido que debe escribirse en el campo.



Ejemplo 8-1 Creación de un campo de texto


```
Label label1 = new Label("Name:");
TextField textField = new TextField ();
HBox hb = new HBox();
hb.getChildren().addAll(label1, textField);
hb.setSpacing(10);
```

Puede crear un campo de texto vacío como se muestra en [Ejemplo 8-1](https://docs.oracle.com/javase/8/javafx/user-interface-tutorial/text-field.htm#BABHCDJJ) o un campo de texto campo con un dato de texto particular en él. Para crear un campo de texto con el texto predefinido, utilice el siguiente constructor de la clase `TextField`: `TextField("¡Hola mundo!")`. Puede obtener el valor de un campo de texto en cualquier momento llamando al método `getText`.

Puede aplicar el método `setPrefColumnCount` de la clase `TextInput` para establecer el tamaño del campo de texto, definido como el número máximo de caracteres que puede mostrar a la vez.





## Construyendo la interfaz de usuario con campos de texto


Normalmente, los objetos `TextField` se usan en formularios para crear varios campos de texto. La aplicación en [Figura 8-2](https://docs.oracle.com/javase/8/javafx/user-interface-tutorial/text-field.htm#BABGGFHG) muestra tres campos de texto y procesa los datos que un usuario ingresa en ellos.



Figura 8-2 Aplicación TextFieldSample

![la aplicación TextBoxSample](https://docs.oracle.com/javase/8/javafx/user-interface-tutorial/img/text-field.png)
[Descripción de "Figura 8-2 Aplicación TextFieldSample"](https://docs.oracle.com/javase/8/javafx/user-interface-tutorial/img_text/text-field.htm)



El fragmento de código en [Ejemplo 8-2](https://docs.oracle.com/javase/8/javafx/user-interface-tutorial/text-field.htm#BABBDFCE) crea los tres campos de texto y dos botones, y los agrega a la escena de la aplicación usando el contenedor `GridPane`. Este contenedor es particularmente útil cuando necesita implementar un diseño flexible para sus controles de interfaz de usuario.



Ejemplo 8-2 Adición de campos de texto a la aplicación


```
//Creating a GridPane container
GridPane grid = new GridPane();
grid.setPadding(new Insets(10, 10, 10, 10));
grid.setVgap(5);
grid.setHgap(5);

//Defining the Name text field
final TextField name = new TextField();
name.setPromptText("Enter your first name.");
GridPane.setConstraints(name, 0, 0);
grid.getChildren().add(name);

//Defining the Last Name text field
final TextField lastName = new TextField();
lastName.setPromptText("Enter your last name.");
GridPane.setConstraints(lastName, 0, 1);
grid.getChildren().add(lastName);

//Defining the Comment text field
final TextField comment = new TextField();
comment.setPromptText("Enter your comment.");
GridPane.setConstraints(comment, 0, 2);
grid.getChildren().add(comment);

//Defining the Submit button
Button submit = new Button("Submit");
GridPane.setConstraints(submit, 1, 0);
grid.getChildren().add(submit);

//Defining the Clear button
Button clear = new Button("Clear");
GridPane.setConstraints(clear, 1, 1);
grid.getChildren().add(clear);
```

Tómese un momento para estudiar el fragmento de código. Los campos de texto `name`, `lastName` y `comment` se crean utilizando constructores vacíos de la clase `TextField`. A diferencia del [Ejemplo 8-1](https://docs.oracle.com/javase/8/javafx/user-interface-tutorial/text-field.htm#BABHCDJJ), las etiquetas no acompañan a los campos de texto en este fragmento de código . En su lugar, los subtítulos notifican a los usuarios qué tipo de datos deben ingresar en los campos de texto. El método `setPromptText` define la cadena que aparece en el campo de texto cuando se inicia la aplicación. Cuando se agrega [Ejemplo 8-2](https://docs.oracle.com/javase/8/javafx/user-interface-tutorial/text-field.htm#BABBDFCE) a la aplicación, se produce el resultado que se muestra en [Figura 8-3](https://docs.oracle.com/javase/8/javafx/user-interface-tutorial/text-field.htm#BABDAAEC).



Figura 8-3 Tres campos de texto con los mensajes de solicitud

![Tres cuadros de texto con el mensaje de texto](https://docs.oracle.com/javase/8/javafx/user-interface-tutorial/img/text-field-prompt.png)
[Descripción de "Figura 8-3 Tres campos de texto con mensajes de solicitud"](https://docs.oracle.com/javase/8/javafx/user-interface-tutorial/img_text/text-field-prompt.htm )



La diferencia entre el texto de solicitud y el texto ingresado en el campo de texto es que el texto de solicitud no se puede obtener a través del método `getText`.

En las aplicaciones de la vida real, los datos ingresados ​​en los campos de texto se procesan de acuerdo con la lógica de una aplicación según lo requiera una tarea comercial específica. La siguiente sección explica cómo usar los campos de texto para evaluar los datos ingresados ​​y generar una respuesta para un usuario.






## Procesamiento de datos de campos de texto

Como se mencionó anteriormente, los datos de texto ingresados ​​por un usuario en los campos de texto se pueden obtener mediante el método `getText` de la clase `TextInput`.

Estudie [Ejemplo 8-3](https://docs.oracle.com/javase/8/javafx/user-interface-tutorial/text-field.htm#BABHHJFF) para aprender a procesar los datos de `TextField` objeto.



Ejemplo 8-3 Definición de acciones para los botones Enviar y Borrar


```
//Adding a Label
final Label label = new Label();
GridPane.setConstraints(label, 0, 3);
GridPane.setColumnSpan(label, 2);
grid.getChildren().add(label);

submit.setOnAction((ActionEvent e) -> {
    if (
        (comment.getText() != null &amp;&amp; !comment.getText().isEmpty())
    ) {
    label.setText(name.getText() + " " +
        lastName.getText() + ", "
        + "thank you for your comment!");
    } else {
        label.setText("You have not left a comment.");
    }
});

clear.setOnAction((ActionEvent e) -> {
    name.clear();
    lastName.clear();
    comment.clear();
    label.setText(null);
});
```

El control `Label` agregado al contenedor `GridPane` muestra la respuesta de una aplicación a los usuarios. Cuando un usuario hace clic en el botón Enviar, el método `setOnAction` comprueba el campo de texto `comentario`. Si contiene una cadena no vacía, se representa un mensaje de agradecimiento. De lo contrario, la aplicación notifica al usuario que el mensaje de comentario aún no se ha dejado, como se muestra en la [Figura 8-4](https://docs.oracle.com/javase/8/javafx/user-interface-tutorial/text -campo.htm#BABBJHIA).



Figura 8-4 El campo de texto de comentario se deja en blanco

![Un cuadro de texto está lleno, dos cuadros de texto están en blanco](https://docs.oracle.com/javase/8/javafx/user-interface-tutorial/img/text-field-nocomment.png)
[Descripción de "Figura 8-4 El campo de texto de comentario dejado en blanco"](https://docs.oracle.com/javase/8/javafx/user-interface-tutorial/img_text/text-field-nocomment.htm)



Cuando un usuario hace clic en el botón Borrar, el contenido se borra en los tres campos de texto.

Revise algunos métodos útiles que puede usar con los campos de texto.

- `copy()`– transfiere el rango actualmente seleccionado en el texto al portapapeles, dejando la selección actual.
- `cut()`– transfiere el rango actualmente seleccionado en el texto al portapapeles, eliminando la selección actual.
- `selectAll()` - selecciona todo el texto en la entrada de texto.
- `pegar()`– transfiere el contenido del portapapeles a este texto, reemplazando la selección actual.


## Mucho más

https://docs.oracle.com/javase/8/javafx/user-interface-tutorial/ui_controls.htm

# Diseño

## VBox y HBox

El diseño en JavaFX comienza con la selección de los controles de contenedor correctos. Los dos controles de diseño que uso con más frecuencia son `VBox`y `HBox`. `VBox`es un contenedor que organiza a sus hijos en una pila vertical. `HBox`ordena a sus hijos en una fila horizontal. El poder de estos dos controles proviene de envolverlos y establecer algunas propiedades clave: alineación, hgrow y vgrow.

Este artículo demostrará estos controles a través de un proyecto de ejemplo. Una maqueta del proyecto muestra una interfaz de usuario con lo siguiente:

- Una fila de controles superiores que contiene Actualizar `Button`y Cerrar sesión `Hyperlink`,
- A `TableView`que crecerá para ocupar el espacio vertical adicional, y
- Un cierre `Button`.

La interfaz de usuario también presenta un `Separator`panel que divide la parte superior de la pantalla con lo que puede convertirse en un panel inferior estándar (Guardar `Button`, Cancelar `Button`, etc.) para la aplicación.

![maqueta de vboxandhboxapp](https://fxdocs.github.io/docs/html5/images/layout/vboxandhboxapp_mockup.png)

### Estructura

A `VBox`es el contenedor más externo "vbox". Este será el `Parent`proporcionado a la Escena. El simple hecho de colocar los controles de la interfaz de usuario en esto `VBox`permitirá que los controles, sobre todo el `TableView` , se estiren para adaptarse al espacio horizontal disponible. Los controles superiores, Actualizar `Button`y Cerrar sesión `Hyperlink`, están envueltos en un archivo `HBox`. Del mismo modo, envuelvo el cierre inferior `Button`en un `HBox`, lo que permite botones adicionales.

```java
VBox vbox = new VBox();

Button btnRefresh = new Button("Refresh");

HBox topRightControls = new HBox();
topRightControls.getChildren().add( signOutLink );

topControls.getChildren().addAll( btnRefresh, topRightControls );

TableView<Customer> tblCustomers = new TableView<>();
Separator sep = new Separator();

HBox bottomControls = new HBox();

Button btnClose = new Button("Close");

bottomControls.getChildren().add( btnClose );

vbox.getChildren().addAll(
        topControls,
        tblCustomers,
        sep,
        bottomControls
);
```

Esta imagen muestra la maqueta desglosada por contenedor. El padre `VBox`es el rectángulo azul más externo. Los HBoxes son los rectángulos interiores (rojo y verde).

![vboxandhboxapp desglosado](https://fxdocs.github.io/docs/html5/images/layout/vboxandhboxapp_brokendown.png)         

### Alineación y Hgrow

Actualizar `Button`está alineado a la izquierda mientras que Cerrar sesión `Hyperlink`está alineado a la derecha. Esto se logra usando dos HBoxes. topControls es un `HBox`que contiene Actualizar `Button`y también contiene un `HBox`con Cerrar sesión `Hyperlink`. A medida que la pantalla se hace más ancha, Cerrar sesión `Hyperlink`se desplazará hacia la derecha, mientras que Actualizar `Button`mantendrá su alineación izquierda.

La alineación es la propiedad que le dice a un contenedor dónde colocar un control. topControls establece la alineación en BOTTOM_LEFT. topRightControls establece la alineación con BOTTOM_RIGHT. "BOTTOM" se asegura de que la línea de base del texto "Actualizar" coincida con la línea de base del texto "Cerrar sesión".

Para que el cierre de sesión `Hyperlink`se mueva hacia la derecha cuando la pantalla se ensancha, `Priority.ALWAYS`es necesario. Esta es una señal para que JavaFX amplíe topRightControls. De lo contrario, topControls mantendrá el espacio y topRightControls aparecerá a la izquierda. Cerrar sesión `Hyperlink`todavía estaría alineado a la derecha pero en un contenedor más estrecho.

Tenga en cuenta que `setHgrow()`es un método estático y no se invoca en topControls `HBox`ni en sí mismo, topRightControls. Esta es una faceta de la API de JavaFX que puede resultar confusa porque la mayoría de las API establece propiedades a través de setters en objetos.

```java
topControls.setAlignment( Pos.BOTTOM_LEFT );

HBox.setHgrow(topRightControls, Priority.ALWAYS );
topRightControls.setAlignment( Pos.BOTTOM_RIGHT );
```

Close `Button`se envuelve en un `HBox`y se posiciona usando la prioridad BOTTOM_RIGHT.

```java
bottomControls.setAlignment(Pos.BOTTOM_RIGHT );
```

### crecer

Dado que el contenedor más externo es `VBox`, el niño `TableView`se expandirá para ocupar espacio horizontal adicional cuando se amplíe la ventana. Sin embargo, cambiar el tamaño vertical de la ventana producirá un espacio en la parte inferior de la pantalla. `VBox`no cambia automáticamente el tamaño de ninguno de sus elementos secundarios . Al igual que con topRightControls `HBox`, se puede configurar un indicador de crecimiento. En el caso del `HBox`, se trataba de una instrucción de cambio de tamaño horizontal setHgrow(). Para el `TableView`contenedor `VBox`, será setVgrow().

```java
VBox.setVgrow( tblCustomers, Priority.ALWAYS );
```

### Margen

Hay algunas formas de espaciar los controles de la interfaz de usuario. Este artículo usa la propiedad margin en varios de los contenedores para agregar espacios en blanco alrededor de los controles. Estos se configuran individualmente en lugar de usar un espacio en el `VBox`para que el Separador abarque todo el ancho.

```java
VBox.setMargin( topControls, new Insets(10.0d) );
VBox.setMargin( tblCustomers, new Insets(0.0d, 10.0d, 10.0d, 10.0d) );
VBox.setMargin( bottomControls, new Insets(10.0d) );
```

El `Insets` usado por tblCustomers omite cualquier espacio superior para mantener el espacio uniforme. JavaFX no consolida los espacios en blanco como en el diseño web. Si el Recuadro superior se estableciera en 10.0d para el `TableView`, la distancia entre los controles superiores y el `TableView`sería el doble de ancha que la distancia entre cualquiera de los otros controles.

Tenga en cuenta que estos son métodos estáticos como el `Priority`.

Esta imagen muestra la aplicación cuando se ejecuta en su tamaño inicial de 800x600.

![captura de pantalla de vboxandhboxapp](https://fxdocs.github.io/docs/html5/images/layout/vboxandhboxapp_screenshot.png)         

Esta imagen muestra la aplicación redimensionada a un alto y ancho más pequeños.

![vboxandhboxapp captura de pantalla SM](https://fxdocs.github.io/docs/html5/images/layout/vboxandhboxapp_screenshot_sm.png)

### Seleccione los contenedores correctos

La filosofía del diseño de JavaFX es la misma que la filosofía de Swing. Seleccione el contenedor adecuado para la tarea en cuestión. Este artículo presentó los dos contenedores más versátiles: `VBox`y HBox. Al establecer propiedades como alineación, hgrow y vgrow, puede crear diseños increíblemente complejos mediante el anidamiento. Estos son los contenedores que más uso y, a menudo, son los únicos contenedores que necesito.

### Código completo

El código se puede probar en un par de archivos .java. Hay un POJO para el objeto Cliente utilizado por el`TableView`

```java
public class Customer {

    private String firstName;
    private String lastName;

    public Customer(String firstName,
                    String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
```

`Application` Esta es la subclase JavaFX completa y principal.

```java
public class VBoxAndHBoxApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        VBox vbox = new VBox();

        HBox topControls = new HBox();
        VBox.setMargin( topControls, new Insets(10.0d) );
        topControls.setAlignment( Pos.BOTTOM_LEFT );

        Button btnRefresh = new Button("Refresh");

        HBox topRightControls = new HBox();
        HBox.setHgrow(topRightControls, Priority.ALWAYS );
        topRightControls.setAlignment( Pos.BOTTOM_RIGHT );
        Hyperlink signOutLink = new Hyperlink("Sign Out");
        topRightControls.getChildren().add( signOutLink );

        topControls.getChildren().addAll( btnRefresh, topRightControls );

        TableView<Customer> tblCustomers = new TableView<>();
        tblCustomers.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        VBox.setMargin( tblCustomers, new Insets(0.0d, 10.0d, 10.0d, 10.0d) );
        VBox.setVgrow( tblCustomers, Priority.ALWAYS );

        TableColumn<Customer, String> lastNameCol = new TableColumn<>("Last Name");
        lastNameCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));

        TableColumn<Customer, String> firstNameCol = new TableColumn<>("First Name");
        firstNameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));

        tblCustomers.getColumns().addAll( lastNameCol, firstNameCol );

        Separator sep = new Separator();

        HBox bottomControls = new HBox();
        bottomControls.setAlignment(Pos.BOTTOM_RIGHT );
        VBox.setMargin( bottomControls, new Insets(10.0d) );

        Button btnClose = new Button("Close");

        bottomControls.getChildren().add( btnClose );

        vbox.getChildren().addAll(
                topControls,
                tblCustomers,
                sep,
                bottomControls
        );

        Scene scene = new Scene(vbox );

        primaryStage.setScene( scene );
        primaryStage.setWidth( 800 );
        primaryStage.setHeight( 600 );
        primaryStage.setTitle("VBox and HBox App");
        primaryStage.setOnShown( (evt) -> loadTable(tblCustomers) );
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void loadTable(TableView<Customer> tblCustomers) {
        tblCustomers.getItems().add(new Customer("George", "Washington"));
        tblCustomers.getItems().add(new Customer("Abe", "Lincoln"));
        tblCustomers.getItems().add(new Customer("Thomas", "Jefferson"));
    }
}
```

## StackPane

`StackPane` coloca a sus hijos uno encima de otro. El último agregado `Node`es el más alto. Por defecto `StackPane`alineará los hijos usando `Pos.CENTER`, como se puede ver en la siguiente imagen, donde están los 3 hijos (en orden de suma): `Rectangle`, `Circle`y `Button`.

​         ![centro de apilamiento](https://fxdocs.github.io/docs/html5/images/layout/stackpane_center.png)        

Esta imagen fue producida por el siguiente fragmento:

```java
public class StackPaneApp extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        StackPane pane = new StackPane(
                new Rectangle(200, 100, Color.BLACK),
                new Circle(40, Color.RED),
                new Button("Hello StackPane")
        );

        stage.setScene(new Scene(pane, 300, 300));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
```

Podemos cambiar la alineación predeterminada agregando `pane.setAlignment(Pos.CENTER_LEFT);`para producir el siguiente efecto:

​         ![apilado a la izquierda](https://fxdocs.github.io/docs/html5/images/layout/stackpane_left.png)        

​        Figura 23. StackPane alineado a la izquierda       

## Posicionamiento absoluto con panel

Contenedores como `VBox`o `BorderPane`alinear y distribuir a sus hijos. La superclase `Pane`también es un contenedor, pero no impone un orden a sus hijos. Los hijos se posicionan a sí mismos a través de propiedades como x, centerX y layoutX. Esto se llama posicionamiento absoluto y es una técnica para colocar un `Shape`o un `Node`en un lugar determinado de la pantalla.

Esta captura de pantalla muestra una vista Acerca de. La vista Acerca de contiene `Hyperlink`en el medio de la pantalla "Acerca de esta aplicación". La vista Acerca de utiliza varias formas JavaFX para formar un diseño que se recorta para que parezca una tarjeta de presentación.

​         ![paneapp sobre la vista](https://fxdocs.github.io/docs/html5/images/layout/paneapp_about_view.png)        

​        Figura 24. Captura de pantalla de la vista Acerca de en PaneApp       

### Tamaño del panel

A diferencia de la mayoría de los contenedores, `Pane`cambia de tamaño para adaptarse a su contenido y no al revés. Esta imagen es una captura de pantalla de Scenic View tomada antes de agregar el Arco inferior derecho. El `Pane`es el área resaltada en amarillo. Tenga en cuenta que no ocupa la totalidad `Stage`.

​          ![paneapp tamaño del panel vista panorámica](https://fxdocs.github.io/docs/html5/images/layout/paneapp_pane_size_scenicview.png)         

​         Figura 25. Vista escénica resaltando la pantalla parcialmente construida        

Esta es una captura de pantalla tomada después de agregar la esquina inferior derecha `Arc`. Esto `Arc`se colocó más cerca del borde inferior derecho del archivo `Stage`. Esto obliga al Panel a estirarse para acomodar los contenidos expandidos.

​          ![Paneapp ScenicView etapa completa](https://fxdocs.github.io/docs/html5/images/layout/paneapp_scenicview_full_stage.png)         

​         Figura 26. Vista escénica resaltando el panel expandido        

### el panel

El contenedor más externo de la vista Acerca de es un `VBox`cuyo único contenido es el archivo `Pane`. El `VBox`se utiliza para encajar en el conjunto `Stage`y proporciona un fondo.

```java
VBox vbox = new VBox();
vbox.setPadding( new Insets( 10 ) );
vbox.setBackground(
    new Background(
        new BackgroundFill(Color.BLACK, new CornerRadii(0), new Insets(0))
        ));

Pane p = new Pane();
```

### Las formas

En la parte superior izquierda de la pantalla, hay un grupo de 4 &apos;Arcos&apos; y 1 &apos;Círculo&apos;. Este código posiciona largeArc en (0,0) a través de los argumentos centerX y centerY en el `Arc`constructor. Observe que backgroundArc también se coloca en (0,0) y aparece debajo de largeArc. `Pane`no intenta eliminar el conflicto de formas superpuestas y, en este caso, lo que se busca es la superposición. smArc1 se coloca en (0,160), que está abajo en el eje Y. smArc2 está posicionado en (160,0) que está justo en el eje X. smCircle se coloca a la misma distancia que smArc1 y smArc2, pero en un ángulo de 45 grados.

```java
Arc largeArc = new Arc(0, 0, 100, 100, 270, 90);
largeArc.setType(ArcType.ROUND);

Arc backgroundArc = new Arc(0, 0, 160, 160, 270, 90 );
backgroundArc.setType( ArcType.ROUND );

Arc smArc1 = new Arc( 0, 160, 30, 30, 270, 180);
smArc1.setType(ArcType.ROUND);

Circle smCircle = new Circle(160/Math.sqrt(2.0), 160/Math.sqrt(2.0), 30,Color.web("0xF2A444"));

Arc smArc2 = new Arc( 160, 0, 30, 30, 180, 180);
smArc2.setType(ArcType.ROUND);
```

La parte inferior derecha `Arc`se coloca en función de la altura total del archivo `Stage`. Los 20 restados de la altura son los 10 píxeles `Insets`de `VBox`(10 para la izquierda + 10 para la derecha).

```java
Arc medArc = new Arc(568-20, 320-20, 60, 60, 90, 90);
medArc.setType(ArcType.ROUND);

primaryStage.setWidth( 568 );
primaryStage.setHeight( 320 );
```

### El hipervínculo

El `Hyperlink`está posicionado compensado el centro (284,160) que es el ancho y alto de `Stage`ambos dividido por dos. Esto coloca el texto del `Hyperlink`en el cuadrante inferior derecho de la pantalla, por lo que se necesita un desplazamiento basado en el `Hyperlink`ancho y el alto. Las dimensiones no están disponibles `Hyperlink`hasta que se muestra la pantalla, por lo que realizo un ajuste posterior a la visualización de la posición.

```java
Hyperlink hyperlink = new Hyperlink("About this App");

primaryStage.setOnShown( (evt) -> {
     hyperlink.setLayoutX( 284 - (hyperlink.getWidth()/3) );
     hyperlink.setLayoutY( 160 - hyperlink.getHeight() );
});
```

El `Hyperlink`no está colocado en el verdadero centro de la pantalla. El valor de layoutX se basa en una operación de división por tres que lo aleja del diseño superior izquierdo.

### Orden Z

Como se mencionó anteriormente, `Pane`admite la superposición de niños. Esta imagen muestra la vista Acerca de con profundidad añadida al diseño superior izquierdo. El más pequeño `Arcs`y `Circle`el cursor sobre backgroundArc al igual que largeArc.

![paneapp zorder](https://fxdocs.github.io/docs/html5/images/layout/paneapp_zorder.png)

El orden z en este ejemplo está determinado por el orden en que se agregan los elementos secundarios al archivo `Pane`. backgroundArc está oscurecido por elementos agregados más tarde, más notablemente largeArc. Para reorganizar los elementos secundarios, use los métodos toFront() y toBack() después de agregar los elementos al archivo `Pane`.

```java
p.getChildren().addAll( backgroundArc, largeArc, smArc1, smCircle, smArc2, hyperlink, medArc );

vbox.getChildren().add( p );
```

Al iniciar JavaFX, es tentador construir un diseño absoluto. Tenga en cuenta que los diseños absolutos son frágiles y, a menudo, se rompen cuando se cambia el tamaño de la pantalla o cuando se agregan elementos durante la fase de mantenimiento del software. Sin embargo, existen buenas razones para utilizar el posicionamiento absoluto. El juego es uno de esos usos. En un juego, puede ajustar la coordenada (x,y) de una &apos;Forma&apos; para mover una pieza del juego por la pantalla. Este artículo demostró la clase JavaFX `Pane`que proporciona un posicionamiento absoluto a cualquier interfaz de usuario basada en formas.

### Código completado

`Application` Esta es la subclase JavaFX completa y principal.

```java
public class PaneApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        VBox vbox = new VBox();
        vbox.setPadding( new Insets( 10 ) );
        vbox.setBackground(
            new Background(
                new BackgroundFill(Color.BLACK, new CornerRadii(0), new Insets(0))
                ));

        Pane p = new Pane();

        Arc largeArc = new Arc(0, 0, 100, 100, 270, 90);
        largeArc.setFill(Color.web("0x59291E"));
        largeArc.setType(ArcType.ROUND);

        Arc backgroundArc = new Arc(0, 0, 160, 160, 270, 90 );
        backgroundArc.setFill( Color.web("0xD96F32") );
        backgroundArc.setType( ArcType.ROUND );

        Arc smArc1 = new Arc( 0, 160, 30, 30, 270, 180);
        smArc1.setFill(Color.web("0xF2A444"));
        smArc1.setType(ArcType.ROUND);

        Circle smCircle = new Circle(
            160/Math.sqrt(2.0), 160/Math.sqrt(2.0), 30,Color.web("0xF2A444")
            );

        Arc smArc2 = new Arc( 160, 0, 30, 30, 180, 180);
        smArc2.setFill(Color.web("0xF2A444"));
        smArc2.setType(ArcType.ROUND);

        Hyperlink hyperlink = new Hyperlink("About this App");
        hyperlink.setFont( Font.font(36) );
        hyperlink.setTextFill( Color.web("0x3E6C93") );
        hyperlink.setBorder( Border.EMPTY );

        Arc medArc = new Arc(568-20, 320-20, 60, 60, 90, 90);
        medArc.setFill(Color.web("0xD9583B"));
        medArc.setType(ArcType.ROUND);

        p.getChildren().addAll( backgroundArc, largeArc, smArc1, smCircle,
            smArc2, hyperlink, medArc );

        vbox.getChildren().add( p );

        Scene scene = new Scene(vbox);
        scene.setFill(Color.BLACK);

        primaryStage.setTitle("Pane App");
        primaryStage.setScene( scene );
        primaryStage.setWidth( 568 );
        primaryStage.setHeight( 320 );
        primaryStage.setOnShown( (evt) -> {
             hyperlink.setLayoutX( 284 - (hyperlink.getWidth()/3) );
             hyperlink.setLayoutY( 160 - hyperlink.getHeight() );
        });
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
```

## Recorte

La mayoría de los contenedores de diseño JavaFX (clase base [Región](https://translate.google.com/website?sl=en&amp;tl=es&amp;hl=ca&amp;client=webapp&amp;u=https://docs.oracle.com/javase/8/javafx/api/javafx/scene/layout/Region.html)) posicionan y dimensionan automáticamente a sus elementos secundarios, por lo que recortar cualquier contenido secundario que pueda sobresalir más allá de los límites del diseño del contenedor nunca es un problema. La gran excepción es [Pane](https://translate.google.com/website?sl=en&amp;tl=es&amp;hl=ca&amp;client=webapp&amp;u=https://docs.oracle.com/javase/8/javafx/api/javafx/scene/layout/Pane.html) , una subclase directa `Region`y la clase base para todos los contenedores de diseño con elementos secundarios de acceso público. A diferencia de sus subclases, Pane no intenta organizar a sus hijos,  sino que simplemente acepta el posicionamiento y el tamaño explícitos del usuario.

Esto lo hace `Pane`adecuado como una superficie de dibujo, similar a [Canvas, pero representa elementos secundarios de ](https://translate.google.com/website?sl=en&amp;tl=es&amp;hl=ca&amp;client=webapp&amp;u=https://docs.oracle.com/javase/8/javafx/api/javafx/scene/canvas/Canvas.html)[Forma](https://translate.google.com/website?sl=en&amp;tl=es&amp;hl=ca&amp;client=webapp&amp;u=https://docs.oracle.com/javase/8/javafx/api/javafx/scene/shape/Shape.html) definidos por el usuario en lugar de comandos de dibujo directos. El problema es que normalmente se espera que las superficies de dibujo recorten automáticamente su contenido en sus límites. `Canvas`hace esto por defecto pero `Pane`no lo hace. Desde el último párrafo de la entrada de Javadoc para `Pane`:

> El panel no recorta su contenido de forma predeterminada, por lo que es posible que los límites de los elementos secundarios se extiendan más allá de sus propios límites, ya sea si los elementos secundarios se colocan en coordenadas negativas o si el panel se redimensiona más pequeño que su tamaño preferido.        

Esta cita es algo engañosa. Los elementos secundarios se representan (total o parcialmente) fuera de su elemento principal `Pane`&apos;siempre que&apos; su combinación de posición y tamaño se extienda más allá de los límites del elemento principal, independientemente de si la posición es negativa o si `Pane`alguna vez se redimensiona. En pocas palabras, `Pane`solo proporciona un cambio de coordenadas a sus elementos secundarios, en función de su esquina superior izquierda, pero sus límites de diseño se ignoran por completo al representar elementos secundarios. Tenga en cuenta que el Javadoc para todas las `Pane`subclases (que revisé) incluye una advertencia similar. Tampoco recortan su contenido, pero como se mencionó anteriormente, esto no suele ser un problema para ellos porque organizan automáticamente a sus hijos.

Entonces, para usarlo correctamente `Pane`como superficie de dibujo para `Shapes`, necesitamos recortar manualmente su contenido. Esto es algo complejo, especialmente cuando se trata de un borde visible. Escribí una pequeña aplicación de demostración para ilustrar el comportamiento predeterminado y varios pasos para solucionarlo. Puede descargarlo como [PaneDemo.zip](https://translate.google.com/website?sl=en&amp;tl=es&amp;hl=ca&amp;client=webapp&amp;u=http://kynosarges.org/misc/PaneDemo.zip) que contiene un proyecto para NetBeans 8.2 y Java SE 8u112. Las siguientes secciones explican cada paso con capturas de pantalla y fragmentos de código pertinentes.

### Comportamiento por defecto

Al comenzar, PaneDemo muestra lo que sucede cuando coloca una `Ellipse`forma en un espacio `Pane`que es demasiado pequeño para contenerla por completo. `Pane`Tiene un bonito borde grueso y redondeado [para](https://translate.google.com/website?sl=en&amp;tl=es&amp;hl=ca&amp;client=webapp&amp;u=https://docs.oracle.com/javase/8/javafx/api/javafx/scene/layout/Border.html) visualizar su área . La ventana de la aplicación es redimensionable, con el `Pane`tamaño siguiendo el tamaño de la ventana. Los tres botones de la izquierda se utilizan para cambiar a los otros pasos de la demostración; haga clic en Predeterminado (Alt+D) para volver a la salida predeterminada de un paso posterior.

​          ![el recorte se extiende](https://fxdocs.github.io/docs/html5/images/layout/clipping_extends.png)         

​         Figura 28. Niño que se extiende fuera de los límites del panel        

Como puede ver, el `Ellipse`sobrescribe el de su padre `Border`y sobresale mucho más allá. El siguiente código se utiliza para generar la vista predeterminada. Se divide en varios métodos más pequeños y una constante para el `Border`radio de la esquina, ya que se hará referencia a ellos en los siguientes pasos.

```java
static final double BORDER_RADIUS = 4;

static Border createBorder() {
    return new Border(
            new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID,
            new CornerRadii(BORDER_RADIUS), BorderStroke.THICK));
}

static Shape createShape() {
    final Ellipse shape = new Ellipse(50, 50);
    shape.setCenterX(80);
    shape.setCenterY(80);
    shape.setFill(Color.LIGHTCORAL);
    shape.setStroke(Color.LIGHTCORAL);
    return shape;
}

static Region createDefault() {
    final Pane pane = new Pane(createShape());
    pane.setBorder(createBorder());
    pane.setPrefSize(100, 100);
    return pane;
}
```

### Recorte simple

Sorprendentemente, no hay una opción predefinida para hacer que un redimensionable `Region`recorte automáticamente a sus elementos secundarios a su tamaño actual. En su lugar, debe usar la [propiedad clipProperty](https://translate.google.com/website?sl=en&amp;tl=es&amp;hl=ca&amp;client=webapp&amp;u=https://docs.oracle.com/javase/8/javafx/api/javafx/scene/Node.html%23clipProperty) básica definida en `Node`y mantenerla actualizada manualmente para reflejar los cambios en los límites del diseño. El método `clipChildren`a continuación muestra cómo funciona esto (con Javadoc porque es posible que desee reutilizarlo en su propio código):

```java
/**
 * Clips the children of the specified {@link Region} to its current size.
 * This requires attaching a change listener to the region’s layout bounds,
 * as JavaFX does not currently provide any built-in way to clip children.
 *
 * @param region the {@link Region} whose children to clip
 * @param arc the {@link Rectangle#arcWidth} and {@link Rectangle#arcHeight}
 *            of the clipping {@link Rectangle}
 * @throws NullPointerException if {@code region} is {@code null}
 */
static void clipChildren(Region region, double arc) {

    final Rectangle outputClip = new Rectangle();
    outputClip.setArcWidth(arc);
    outputClip.setArcHeight(arc);
    region.setClip(outputClip);

    region.layoutBoundsProperty().addListener((ov, oldValue, newValue) -> {
        outputClip.setWidth(newValue.getWidth());
        outputClip.setHeight(newValue.getHeight());
    });
}

static Region createClipped() {
    final Pane pane = new Pane(createShape());
    pane.setBorder(createBorder());
    pane.setPrefSize(100, 100);

    // clipped children still overwrite Border!
    clipChildren(pane, 3 * BORDER_RADIUS);

    return pane;
}
```

Elija Recortado (Alt+C) en PaneDemo para representar la salida correspondiente. Así es como se ve:

​          ![recorte recortado](https://fxdocs.github.io/docs/html5/images/layout/clipping_clipped.png)         

​         Figura 29. Panel con clip aplicado        

Eso es mejor. El `Ellipse`ya no sobresale más allá del `Pane`– pero todavía sobrescribe su Borde. También tenga en cuenta que tuvimos que especificar manualmente un redondeo de esquina estimado para el recorte `Rectangle`para reflejar las `Border`esquinas redondeadas. Esta estimación es 3 * BORDER_RADIUS porque el radio de esquina especificado en `Border`realmente define su radio interior, y el radio exterior (que necesitamos aquí) será mayor dependiendo del `Border`grosor. (Podría calcular el radio exterior exactamente si realmente quisiera, pero lo omití para la aplicación de demostración).

#### 4.4.3. Paneles anidados

¿Podemos de alguna manera especificar una región de recorte que excluya un &apos;Borde&apos; visible? No en el dibujo `Pane`en sí, que yo sepa. La región de recorte afecta `Border`tanto al contenido como a otros, por lo que si tuviera que reducir la región de recorte para excluirla, ya no vería nada `Border`. En su lugar, la solución es crear dos paneles anidados: un dibujo interior `Pane`sin `Border`que se ajuste exactamente a sus límites y otro exterior `StackPane`que defina lo visible `Border`y también cambie el tamaño del dibujo `Pane`. Aquí está el código final:

```java
static Region createNested() {
    // create drawing Pane without Border or size
    final Pane pane = new Pane(createShape());
    clipChildren(pane, BORDER_RADIUS);

    // create sized enclosing Region with Border
    final Region container = new StackPane(pane);
    container.setBorder(createBorder());
    container.setPrefSize(100, 100);
    return container;
}
```

Elija Anidado (Alt+N) en PaneDemo para representar la salida correspondiente. Ahora todo se ve como debería:

![recorte anidado](https://fxdocs.github.io/docs/html5/images/layout/clipping_nested.png)

Como beneficio adicional, ya no necesitamos estimar un radio de esquina correcto para el recorte `Rectangle`. Ahora recortamos la circunferencia interior en lugar de la exterior de nuestro visible `Border`, para que podamos reutilizar directamente su radio de esquina interior. Si especifica múltiples radios de esquina diferentes o uno más complejo `Border`, tendrá que definir un recorte correspondientemente más complejo `Shape`.

Hay una pequeña advertencia. La esquina superior izquierda del dibujo `Pane`con respecto a todas las coordenadas secundarias ahora comienza *dentro* del visible `Border`. Si cambia retroactivamente uno `Pane`con paneles visibles `Border`a anidados como se muestra aquí, todos los niños exhibirán un ligero cambio de posición correspondiente al `Border`grosor.

## GridPane

Los formularios en las aplicaciones comerciales a menudo usan un diseño que imita un registro de base de datos. Para cada columna de una tabla, se agrega un encabezado en el lado izquierdo que coincide con un valor de fila en el lado derecho. JavaFX tiene un control de propósito especial llamado `GridPane`para este tipo de diseño que mantiene los contenidos alineados por fila y columna. `GridPane`también admite expansión para diseños más complejos.

`GridPane`Esta captura de pantalla muestra un diseño básico . En el lado izquierdo del formulario, hay una columna de nombres de campo: Correo electrónico, Prioridad, Problema, Descripción. En el lado derecho del formulario, hay una columna de controles que mostrará el valor del campo correspondiente. Los nombres de campo son de tipo `Label`y los controles de valor son una mezcla que incluye `TextField`, `TextArea`y `ComboBox`.

![aplicación gridpane](https://fxdocs.github.io/docs/html5/images/layout/gridpaneapp.png)

El siguiente código muestra los objetos creados para el formulario. "vbox" es la raíz del `Scene`y también contendrá el `ButtonBar`en la base del formulario.

```java
VBox vbox = new VBox();

GridPane gp = new GridPane();

Label lblTitle = new Label("Support Ticket");

Label lblEmail = new Label("Email");
TextField tfEmail = new TextField();

Label lblPriority = new Label("Priority");
ObservableList<String> priorities = FXCollections.observableArrayList("Medium", "High", "Low");
ComboBox<String> cbPriority = new ComboBox<>(priorities);

Label lblProblem = new Label("Problem");
TextField tfProblem = new TextField();

Label lblDescription = new Label("Description");
TextArea taDescription = new TextArea();
```

GridPane tiene un método útil `setGridLinesVisible()`que muestra la estructura de la cuadrícula y los canalones. Es especialmente útil en diseños más complejos donde se involucra la expansión porque los espacios en las asignaciones de filas/columnas pueden causar cambios en el diseño.

![líneas de gridpaneapp](https://fxdocs.github.io/docs/html5/images/layout/gridpaneapp_lines.png)

### Espaciado

Como contenedor, `GridPane`tiene una propiedad de relleno que se puede configurar para rodear el `GridPane`contenido con espacios en blanco. "relleno" tomará un `Inset`objeto como parámetro. En este ejemplo, se aplican 10 píxeles de espacio en blanco a todos los lados, por lo que se usa un constructor de formato corto para `Inset`.

Dentro de `GridPane`, vgap y hgap controlan los canalones. El hgap se establece en 4 para mantener los campos cerca de sus valores. vgap es un poco más grande para ayudar con la navegación del mouse.

```java
gp.setPadding( new Insets(10) );
gp.setHgap( 4 );
gp.setVgap( 8 );
```

Para mantener consistente la parte inferior del formulario, `Priority`se establece a en el VBox. Sin embargo , esto *no cambiará el tamaño* de las filas individuales. Para especificaciones de cambio de tamaño individuales, use `ColumnConstraints`y `RowConstraints`.

```java
VBox.setVgrow(gp, Priority.ALWAYS );
```

### Adición de elementos

A diferencia de los contenedores como `BorderPane`o `HBox`, los nodos deben especificar su posición dentro del archivo `GridPane`. Esto se hace con el `add()`método en `GridPane`y no con el método add en una propiedad secundaria del contenedor. Esta forma del `GridPane` `add()`método toma una posición de columna de base cero y una posición de fila de base cero. Este código pone dos declaraciones en la misma línea para facilitar la lectura.

```java
gp.add( lblTitle,       1, 1);  // empty item at 0,0
gp.add( lblEmail,       0, 2); gp.add(tfEmail,        1, 2);
gp.add( lblPriority,    0, 3); gp.add( cbPriority,    1, 3);
gp.add( lblProblem,     0, 4); gp.add( tfProblem,     1, 4);
gp.add( lblDescription, 0, 5); gp.add( taDescription, 1, 5);
```

lblTitle se coloca en la segunda columna de la primera fila. No hay ninguna entrada en la primera columna de la primera fila.

Las adiciones posteriores se presentan por parejas. Los objetos de nombre de campo `Label`se colocan en la primera columna (índice de columna=0) y los controles de valor se colocan en la segunda columna (índice de columna=1). Las filas se agregan por el segundo valor incrementado. Por ejemplo, lblPriority se coloca en la cuarta fila junto con su `ComboBox`.

`GridPane`es un contenedor importante en el diseño de aplicaciones empresariales JavaFX. Cuando tenga un requisito de pares de nombre/valor, `GridPane`será una manera fácil de admitir la fuerte orientación de columna de un formulario tradicional.

### Código completado

La siguiente clase es el código completo del ejemplo. Esto incluye la definición de la `ButtonBar`que no se presentó en las secciones anteriores enfocadas en `GridPane`.

```java
public class GridPaneApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        VBox vbox = new VBox();

        GridPane gp = new GridPane();
        gp.setPadding( new Insets(10) );
        gp.setHgap( 4 );
        gp.setVgap( 8 );

        VBox.setVgrow(gp, Priority.ALWAYS );

        Label lblTitle = new Label("Support Ticket");

        Label lblEmail = new Label("Email");
        TextField tfEmail = new TextField();

        Label lblPriority = new Label("Priority");
        ObservableList<String> priorities =
            FXCollections.observableArrayList("Medium", "High", "Low");
        ComboBox<String> cbPriority = new ComboBox<>(priorities);

        Label lblProblem = new Label("Problem");
        TextField tfProblem = new TextField();

        Label lblDescription = new Label("Description");
        TextArea taDescription = new TextArea();

        gp.add( lblTitle,       1, 1);  // empty item at 0,0
        gp.add( lblEmail,       0, 2); gp.add(tfEmail,        1, 2);
        gp.add( lblPriority,    0, 3); gp.add( cbPriority,    1, 3);
        gp.add( lblProblem,     0, 4); gp.add( tfProblem,     1, 4);
        gp.add( lblDescription, 0, 5); gp.add( taDescription, 1, 5);

        Separator sep = new Separator(); // hr

        ButtonBar buttonBar = new ButtonBar();
        buttonBar.setPadding( new Insets(10) );

        Button saveButton = new Button("Save");
        Button cancelButton = new Button("Cancel");

        buttonBar.setButtonData(saveButton, ButtonBar.ButtonData.OK_DONE);
        buttonBar.setButtonData(cancelButton, ButtonBar.ButtonData.CANCEL_CLOSE);

        buttonBar.getButtons().addAll(saveButton, cancelButton);

        vbox.getChildren().addAll( gp, sep, buttonBar );

        Scene scene = new Scene(vbox);

        primaryStage.setTitle("Grid Pane App");
        primaryStage.setScene(scene);
        primaryStage.setWidth( 736 );
        primaryStage.setHeight( 414  );
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
```

## GridPane Spanning

Para formularios más complejos implementados con `GridPane`, se admite la expansión. La expansión permite que un control reclame el espacio de columnas vecinas (colspan) y filas vecinas (rowspan). Esta captura de pantalla muestra un formulario que amplía el ejemplo de la sección anterior. El diseño de dos columnas de la versión anterior se reemplazó por un diseño de varias columnas. Los campos como Problema y Descripción conservan la estructura original. Pero se agregaron controles a las filas que anteriormente contenían solo Correo electrónico y Prioridad.

​         ![complejogridpaneapp](https://fxdocs.github.io/docs/html5/images/layout/complexgridpaneapp.png)        

​        Figura 33. Columnas de expansión       

Al activar las líneas de la cuadrícula, observe que la cuadrícula anterior de dos columnas se reemplaza con una cuadrícula de seis columnas. La tercera fila que contiene seis elementos (3 pares de nombre de campo/valor) dicta la estructura. El resto del formulario utilizará la expansión para completar el espacio en blanco.

​         ![líneas complejas de gridpaneapp](https://fxdocs.github.io/docs/html5/images/layout/complexgridpaneapp_lines.png)        

​        Figura 34. Líneas que resaltan la extensión       

A continuación se muestran los objetos contenedor `VBox`y `GridPane`utilizados en esta actualización. Hay un poco más de Vgap para ayudar al usuario a seleccionar los `ComboBox`controles.

```java
GridPane gp = new GridPane();
gp.setPadding( new Insets(10) );
gp.setHgap( 4 );
gp.setVgap( 10 );

VBox.setVgrow(gp, Priority.ALWAYS );
```

Estas son declaraciones de creación de control del ejemplo actualizado.

```java
Label lblTitle = new Label("Support Ticket");

Label lblEmail = new Label("Email");
TextField tfEmail = new TextField();

Label lblContract = new Label("Contract");
TextField tfContract = new TextField();

Label lblPriority = new Label("Priority");
ObservableList<String> priorities =
    FXCollections.observableArrayList("Medium", "High", "Low");
ComboBox<String> cbPriority = new ComboBox<>(priorities);

Label lblSeverity = new Label("Severity");
ObservableList<String> severities =
    FXCollections.observableArrayList("Blocker", "Workaround", "N/A");
ComboBox<String> cbSeverity = new ComboBox<>(severities);

Label lblCategory = new Label("Category");
ObservableList<String> categories =
    FXCollections.observableArrayList("Bug", "Feature");
ComboBox<String> cbCategory = new ComboBox<>(categories);

Label lblProblem = new Label("Problem");
TextField tfProblem = new TextField();

Label lblDescription = new Label("Description");
TextArea taDescription = new TextArea();
```

Como en la versión anterior, los controles se agregan al `GridPane`método `add()`. Se especifica una columna y una fila. En este fragmento, la indexación no es sencilla, ya que se espera que se llenen los vacíos mediante el contenido expandido.

```java
gp.add( lblTitle,       1, 0);  // empty item at 0,0

gp.add( lblEmail,       0, 1);
gp.add(tfEmail,         1, 1);
gp.add( lblContract,    4, 1 );
gp.add( tfContract,     5, 1 );

gp.add( lblPriority,    0, 2);
gp.add( cbPriority,     1, 2);
gp.add( lblSeverity,    2, 2);
gp.add( cbSeverity,     3, 2);
gp.add( lblCategory,    4, 2);
gp.add( cbCategory,     5, 2);

gp.add( lblProblem,     0, 3); gp.add( tfProblem,     1, 3);
gp.add( lblDescription, 0, 4); gp.add( taDescription, 1, 4);
```

Finalmente, las definiciones de expansión se establecen mediante un método estático en `GridPane`. Hay un método similar para hacer la expansión de filas. El título ocupará 5 columnas, al igual que el problema y la descripción. El correo electrónico comparte una fila con el contrato, pero ocupará más columnas. La tercera fila de ComboBoxes es un conjunto de tres pares de campo/valor, cada uno de los cuales ocupa una columna.

```java
GridPane.setColumnSpan( lblTitle, 5 );
GridPane.setColumnSpan( tfEmail, 3 );
GridPane.setColumnSpan( tfProblem, 5 );
GridPane.setColumnSpan( taDescription, 5 );
```

Alternativamente, una variación del método add() tendrá argumentos columnSpan y rowSpan para evitar la subsiguiente llamada al método estático.

Este ejemplo ampliado `GridPane`demostró la expansión de columnas. La misma capacidad está disponible para la expansión de filas, lo que permitiría que un control reclame espacio vertical adicional. La expansión mantiene los controles alineados incluso en los casos en que varía el número de elementos en una fila (o columna) determinada. Para mantener el enfoque en el tema de expansión, esta cuadrícula permitió que variaran los anchos de las columnas. El artículo sobre `ColumnConstraints`y `RowConstraints`se centrará en la construcción de verdaderas cuadrículas tipográficas modulares y de columnas mediante un mejor control de las columnas (y las filas).

### Código completado

El siguiente es el código completo para el ejemplo de GridPane de expansión.

```java
public class ComplexGridPaneApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        VBox vbox = new VBox();

        GridPane gp = new GridPane();
        gp.setPadding( new Insets(10) );
        gp.setHgap( 4 );
        gp.setVgap( 10 );

        VBox.setVgrow(gp, Priority.ALWAYS );

        Label lblTitle = new Label("Support Ticket");

        Label lblEmail = new Label("Email");
        TextField tfEmail = new TextField();

        Label lblContract = new Label("Contract");
        TextField tfContract = new TextField();

        Label lblPriority = new Label("Priority");
        ObservableList<String> priorities =
            FXCollections.observableArrayList("Medium", "High", "Low");
        ComboBox<String> cbPriority = new ComboBox<>(priorities);

        Label lblSeverity = new Label("Severity");
        ObservableList<String> severities = FXCollections.observableArrayList("Blocker", "Workaround", "N/A");
        ComboBox<String> cbSeverity = new ComboBox<>(severities);

        Label lblCategory = new Label("Category");
        ObservableList<String> categories = FXCollections.observableArrayList("Bug", "Feature");
        ComboBox<String> cbCategory = new ComboBox<>(categories);

        Label lblProblem = new Label("Problem");
        TextField tfProblem = new TextField();

        Label lblDescription = new Label("Description");
        TextArea taDescription = new TextArea();

        gp.add( lblTitle,       1, 0);  // empty item at 0,0

        gp.add( lblEmail,       0, 1);
        gp.add(tfEmail,         1, 1);
        gp.add( lblContract,    4, 1 );
        gp.add( tfContract,     5, 1 );

        gp.add( lblPriority,    0, 2);
        gp.add( cbPriority,     1, 2);
        gp.add( lblSeverity,    2, 2);
        gp.add( cbSeverity,     3, 2);
        gp.add( lblCategory,    4, 2);
        gp.add( cbCategory,     5, 2);

        gp.add( lblProblem,     0, 3); gp.add( tfProblem,     1, 3);
        gp.add( lblDescription, 0, 4); gp.add( taDescription, 1, 4);

        GridPane.setColumnSpan( lblTitle, 5 );
        GridPane.setColumnSpan( tfEmail, 3 );
        GridPane.setColumnSpan( tfProblem, 5 );
        GridPane.setColumnSpan( taDescription, 5 );

        Separator sep = new Separator(); // hr

        ButtonBar buttonBar = new ButtonBar();
        buttonBar.setPadding( new Insets(10) );

        Button saveButton = new Button("Save");
        Button cancelButton = new Button("Cancel");

        buttonBar.setButtonData(saveButton, ButtonBar.ButtonData.OK_DONE);
        buttonBar.setButtonData(cancelButton, ButtonBar.ButtonData.CANCEL_CLOSE);

        buttonBar.getButtons().addAll(saveButton, cancelButton);

        vbox.getChildren().addAll( gp, sep, buttonBar );

        Scene scene = new Scene(vbox);

        primaryStage.setTitle("Grid Pane App");
        primaryStage.setScene(scene);
        primaryStage.setWidth( 736 );
        primaryStage.setHeight( 414  );
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
```

## Restricciones de fila y columna de GridPane

Los artículos anteriores sobre `GridPane`cómo crear un diseño de dos columnas con nombres de campo en el lado izquierdo y valores de campo en el lado derecho. Ese ejemplo se amplió para agregar más controles a una fila determinada y para usar espacios en el contenido del controlador de expansión. Este artículo presenta un par de clases JavaFX `ColumnConstraints`y `RowConstraints`. Estas clases dan especificaciones adicionales a una fila o columna. En este ejemplo, una fila que contiene un `TextArea`tendrá todo el espacio adicional cuando se cambie el tamaño de la ventana. Las dos columnas se establecerán en anchos iguales.

Esta captura de pantalla muestra un ejemplo modificado de artículos anteriores. El programa de demostración de este artículo tiene una sensación rotativa en la que los nombres de los campos se emparejan con los valores de campo verticalmente (sobre los valores) en lugar de horizontalmente. La expansión de filas y columnas se usa para alinear elementos que son más grandes que una sola celda.

​         ![restriccionesgridpaneapp 1 anotado](https://fxdocs.github.io/docs/html5/images/layout/constraintsgridpaneapp_1_annotated.png)        

Los rectángulos rojos y el texto no forman parte de la interfaz de usuario. Están identificando secciones de la pantalla que se abordarán más adelante con ColumnConstraints y RowConstaints.

Este código es la creación de la `Scene`raíz y los `GridPane`objetos.

```java
VBox vbox = new VBox();

GridPane gp = new GridPane();
gp.setPadding( new Insets(10) );
gp.setHgap( 4 );
gp.setVgap( 10 );

VBox.setVgrow(gp, Priority.ALWAYS );
```

Este código crea los objetos de control de la interfaz de usuario que se usan en el artículo. Tenga en cuenta que Priority ahora se implementa como un `VBox`RadioButtons contenedor.

```java
Label lblTitle = new Label("Support Ticket");

Label lblEmail = new Label("Email");
TextField tfEmail = new TextField();

Label lblContract = new Label("Contract");
TextField tfContract = new TextField();

Label lblPriority = new Label("Priority");
RadioButton rbMedium = new RadioButton("Medium");
RadioButton rbHigh = new RadioButton("High");
RadioButton rbLow = new RadioButton("Low");
VBox priorityVBox = new VBox();
priorityVBox.setSpacing( 2 );
GridPane.setVgrow(priorityVBox, Priority.SOMETIMES);
priorityVBox.getChildren().addAll( lblPriority, rbMedium, rbHigh, rbLow );

Label lblSeverity = new Label("Severity");
ObservableList<String> severities =
    FXCollections.observableArrayList("Blocker", "Workaround", "N/A");
ComboBox<String> cbSeverity = new ComboBox<>(severities);

Label lblCategory = new Label("Category");
ObservableList<String> categories =
    FXCollections.observableArrayList("Bug", "Feature");
ComboBox<String> cbCategory = new ComboBox<>(categories);

Label lblProblem = new Label("Problem");
TextField tfProblem = new TextField();

Label lblDescription = new Label("Description");
TextArea taDescription = new TextArea();
```

Los pares de control de etiqueta y valor de correo electrónico, contrato,  problema y descripción se colocan en una sola columna. Deben tomar el ancho completo del de `GridPane`modo que cada uno tenga su columnSpan establecido en 2.

```java
GridPane.setColumnSpan( tfEmail, 2 );
GridPane.setColumnSpan( tfContract, 2 );
GridPane.setColumnSpan( tfProblem, 2 );
GridPane.setColumnSpan( taDescription, 2 );
```

Los nuevos botones de opción de prioridad se combinan horizontalmente con cuatro controles de gravedad y categoría. Esta configuración de rowSpan le indica a JavaFX que coloque el VBox que contiene el RadioButton en una celda combinada que tiene cuatro filas de altura.

```java
GridPane.setRowSpan( priorityVBox, 4 );
```

### Restricciones de fila

En este punto, el código refleja la captura de pantalla de la interfaz de usuario que se presenta en [Ejemplo de aplicación que usa filas y columnas](https://fxdocs-github-io.translate.goog/docs/html5/?_x_tr_sl=en&amp;_x_tr_tl=es&amp;_x_tr_hl=ca&amp;_x_tr_pto=wapp#initial_image) . Para reasignar el espacio adicional en la base del formulario, use un objeto RowConstraints para establecer Priority.ALWAYS en la fila del `TextArea`. Esto dará como resultado el `TextArea`crecimiento para llenar el espacio disponible con algo utilizable.

​          ![restriccionesgridpaneapp descripción](https://fxdocs.github.io/docs/html5/images/layout/constraintsgridpaneapp_description.png)         

​         Figura 36. TextArea crece para llenar espacio adicional        

Este código es un `RowConstraints`objeto para el `GridPane`para el `TextArea`. Antes del colocador, `RowConstraints`se asignan objetos para todas las demás filas. El método set de `getRowConstraints()`arrojará una excepción de índice cuando especifique la fila 12 sin asignar primero un objeto.

```java
RowConstraints taDescriptionRowConstraints = new RowConstraints();
taDescriptionRowConstraints.setVgrow(Priority.ALWAYS);

for( int i=0; i<13; i++ ) {
    gp.getRowConstraints().add( new RowConstraints() );
}

gp.getRowConstraints().set( 12, taDescriptionRowConstraints );
```

Como sintaxis alternativa, hay un método setConstraints() disponible en `GridPane`. Esto pasará varios valores y obviará la necesidad de la llamada dedicada columnSpan set para `TextArea`. El `RowConstraints`código del listado anterior no aparecerá en el programa terminado.

```java
gp.setConstraints(taDescription,
                  0, 12,
                  2, 1,
                  HPos.LEFT, VPos.TOP,
                  Priority.SOMETIMES, Priority.ALWAYS);
```

Este código identifica el `Node`at (0,12) que es el `TextArea`. El `TextArea`abarcará 2 columnas pero solo 1 fila. Los HPos y Vpos están configurados en la PARTE SUPERIOR IZQUIERDA. Finalmente, el `Priority`de hgrow es A VECES y el de vgrow es SIEMPRE. Dado que `TextArea`es la única fila con "SIEMPRE", obtendrá el espacio adicional. Si hubiera otras configuraciones SIEMPRE, el espacio se compartiría entre varias filas.

### Restricciones de columna

Para asignar correctamente el espacio que rodea los controles de Severidad y Categoría, se especificarán ColumnConstraints. El comportamiento predeterminado asigna menos espacio a la primera columna debido a los botones de opción de prioridad más pequeños. La siguiente estructura alámbrica muestra el diseño deseado que tiene columnas iguales separadas por un margen de 4 píxeles (Hgap).

​          ![restriccionesgridpaneapp estructura alámbrica](https://fxdocs.github.io/docs/html5/images/layout/constraintsgridpaneapp_wireframe.png)         

​         Figura 37. Wireframe de la aplicación de demostración        

Para que los anchos de las columnas sean iguales, defina dos `ColumnConstraint`objetos y use un especificador de porcentaje.

```java
ColumnConstraints col1 = new ColumnConstraints();
col1.setPercentWidth( 50 );
ColumnConstraints col2 = new ColumnConstraints();
col2.setPercentWidth( 50 );
gp.getColumnConstraints().addAll( col1, col2 );
```

Esta es una captura de pantalla del ejemplo terminado.

​          ![restriccionesgridpaneapp terminado](https://fxdocs.github.io/docs/html5/images/layout/constraintsgridpaneapp_finished.png)         

`GridPane`es un control importante en el desarrollo de aplicaciones empresariales JavaFX. Cuando trabaje en un requisito que involucre pares de nombre/valor y una sola vista de registro, use `GridPane`. Si bien `GridPane`es más fácil de usar que el `GridBagLayout`de Swing, todavía encuentro que la API es un poco inconveniente (asignación de índices propios, restricciones disociadas). Afortunadamente, existe Scene Builder que simplifica enormemente la construcción de este formulario.

### Código completado

```java
public class ConstraintsGridPaneApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        VBox vbox = new VBox();

        GridPane gp = new GridPane();
        gp.setPadding( new Insets(10) );
        gp.setHgap( 4 );
        gp.setVgap( 10 );

        VBox.setVgrow(gp, Priority.ALWAYS );

        Label lblTitle = new Label("Support Ticket");

        Label lblEmail = new Label("Email");
        TextField tfEmail = new TextField();

        Label lblContract = new Label("Contract");
        TextField tfContract = new TextField();

        Label lblPriority = new Label("Priority");
        RadioButton rbMedium = new RadioButton("Medium");
        RadioButton rbHigh = new RadioButton("High");
        RadioButton rbLow = new RadioButton("Low");
        VBox priorityVBox = new VBox();
        priorityVBox.setSpacing( 2 );
        GridPane.setVgrow(priorityVBox, Priority.SOMETIMES);
        priorityVBox.getChildren().addAll( lblPriority, rbMedium, rbHigh, rbLow );

        Label lblSeverity = new Label("Severity");
        ObservableList<String> severities = FXCollections.observableArrayList("Blocker", "Workaround", "N/A");
        ComboBox<String> cbSeverity = new ComboBox<>(severities);

        Label lblCategory = new Label("Category");
        ObservableList<String> categories = FXCollections.observableArrayList("Bug", "Feature");
        ComboBox<String> cbCategory = new ComboBox<>(categories);

        Label lblProblem = new Label("Problem");
        TextField tfProblem = new TextField();

        Label lblDescription = new Label("Description");
        TextArea taDescription = new TextArea();

        gp.add( lblTitle,       0, 0);

        gp.add( lblEmail,       0, 1);
        gp.add(tfEmail,         0, 2);

        gp.add( lblContract,    0, 3 );
        gp.add( tfContract,     0, 4 );

        gp.add( priorityVBox,   0, 5);

        gp.add( lblSeverity,    1, 5);
        gp.add( cbSeverity,     1, 6);
        gp.add( lblCategory,    1, 7);
        gp.add( cbCategory,     1, 8);

        gp.add( lblProblem,     0, 9);
        gp.add( tfProblem,      0, 10);

        gp.add( lblDescription, 0, 11);
        gp.add( taDescription,  0, 12);

        GridPane.setColumnSpan( tfEmail, 2 );
        GridPane.setColumnSpan( tfContract, 2 );
        GridPane.setColumnSpan( tfProblem, 2 );

        GridPane.setRowSpan( priorityVBox, 4 );

        gp.setConstraints(taDescription,
                          0, 12,
                          2, 1,
                          HPos.LEFT, VPos.TOP,
                          Priority.SOMETIMES, Priority.ALWAYS);

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth( 50 );
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth( 50 );
        gp.getColumnConstraints().addAll( col1, col2 );

        Separator sep = new Separator(); // hr

        ButtonBar buttonBar = new ButtonBar();
        buttonBar.setPadding( new Insets(10) );

        Button saveButton = new Button("Save");
        Button cancelButton = new Button("Cancel");

        buttonBar.setButtonData(saveButton, ButtonBar.ButtonData.OK_DONE);
        buttonBar.setButtonData(cancelButton, ButtonBar.ButtonData.CANCEL_CLOSE);

        buttonBar.getButtons().addAll(saveButton, cancelButton);

        vbox.getChildren().addAll( gp, sep, buttonBar );

        Scene scene = new Scene(vbox);

        primaryStage.setTitle("Grid Pane App");
        primaryStage.setScene(scene);
        primaryStage.setWidth( 414 );
        primaryStage.setHeight( 736  );
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
```

## AnchorPane

`AnchorPane`es un control contenedor que define su diseño en términos de bordes. Cuando se coloca en un contenedor, `AnchorPane`se estira para llenar el espacio disponible. Los hijos de `AnchorPane`expresan sus posiciones y tamaños como distancias desde los bordes: Arriba, Izquierda, Abajo, Derecha. Si se colocan una o dos configuraciones de anclaje en un `AnchorPane`niño, el niño se fijará a esa esquina de la ventana. Si se utilizan más de dos configuraciones de anclaje, el niño se estirará  para llenar el espacio horizontal y vertical disponible.

Esta maqueta muestra un `TextArea`rodeado por un conjunto de controles: un `Hyperlink`y dos indicadores de estado. Dado `TextArea`que contendrá todo el contenido, debería ocupar la mayor parte del espacio inicialmente y debería adquirir cualquier espacio adicional de un cambio de tamaño. En la periferia, hay una `Hyperlink`en la parte superior derecha, una conexión `Label`y `Circle`en la parte inferior derecha y un estado `Label`en la parte inferior izquierda.

​         ![maqueta de la aplicación Anchorpane](https://fxdocs.github.io/docs/html5/images/layout/anchorpaneapp_mockup.png)        

​        Figura 39. AnchorPane con TextArea       

### anclas

Para comenzar el diseño, cree un `AnchorPane`objeto y agréguelo al archivo `Scene`.

```java
AnchorPane ap = new AnchorPane();
Scene scene = new Scene(ap);
```

Los anclajes se establecen mediante métodos estáticos de la clase AnchorPane. Los métodos, uno por borde, aceptan el `Node`y un desplazamiento. Para el `Hyperlink`, se establecerá un ancla en el borde superior y otra en el borde derecho. Se establece un desplazamiento de 10,0 para cada borde para que el enlace no se comprima contra el lado.

```java
Hyperlink signoutLink = new Hyperlink("Sign Out");

ap.getChildren().add( signoutLink );

AnchorPane.setTopAnchor( signoutLink, 10.0d );
AnchorPane.setRightAnchor( signoutLink, 10.0d );
```

Cuando se cambia el tamaño de la pantalla, AnchorPane cambiará de tamaño y signoutLink mantendrá su posición superior derecha. Debido a que no se especifican los anclajes izquierdo ni inferior, signoutLink no se estirará.

A continuación, se añaden la conexión `Label`y `Circle`. Estos controles están envueltos en un archivo `HBox`.

```java
Circle circle = new Circle();
circle.setFill(Color.GREEN );
circle.setRadius(10);

Label connLabel = new Label("Connection");

HBox connHBox = new HBox();
connHBox.setSpacing( 4.0d );
connHBox.setAlignment(Pos.BOTTOM_RIGHT);
connHBox.getChildren().addAll( connLabel, circle );

AnchorPane.setBottomAnchor( connHBox, 10.0d );
AnchorPane.setRightAnchor( connHBox, 10.0d );

ap.getChildren().add( connHBox );
```

Al igual que con signoutLink, connHBox se fija en un lugar de la pantalla. connHBox se establece en 10 píxeles desde el borde inferior y 10 píxeles desde el borde derecho.

Se agrega el estado inferior izquierdo `Label`. Los anclajes izquierdo e inferior están establecidos.

```java
Label statusLabel = new Label("Program status");
ap.getChildren().add( statusLabel );

AnchorPane.setBottomAnchor( statusLabel, 10.0d );
AnchorPane.setLeftAnchor( statusLabel, 10.0d );
```

Esta es una captura de pantalla de la aplicación terminada. Las etiquetas de estado y control se encuentran en la parte inferior de la pantalla, fijadas a los bordes izquierdo y derecho respectivamente. está anclado en la `Hyperlink`parte superior derecha.

​          ![predeterminado de la aplicación Anchorpane](https://fxdocs.github.io/docs/html5/images/layout/anchorpaneapp_default.png)         

### Cambiar el tamaño

Los controles en la periferia pueden variar en tamaño. Por ejemplo, un mensaje de estado o un mensaje de conexión puede ser más largo. Sin embargo, la longitud adicional se puede acomodar en este diseño extendiendo el estado de la parte inferior izquierda `Label`hacia la derecha y extendiendo el estado de conexión de la parte inferior derecha `Label`hacia la izquierda. Cambiar el tamaño con este diseño moverá estos controles en términos absolutos, pero se adherirán a sus respectivos bordes más el desplazamiento.

Ese no es el caso con el `TextArea`. Debido a que `TextArea`puede contener una gran cantidad de contenido, debe recibir cualquier espacio adicional que el usuario le dé a la ventana. Este control estará anclado a las cuatro esquinas del `AnchorPane`. Esto hará `TextArea`que cambie el tamaño cuando la ventana cambie de tamaño. se fija en la `TextArea`parte superior izquierda y, a medida que el usuario arrastra los controladores de la ventana hacia la parte inferior derecha, la esquina inferior derecha de los `TextArea`movimientos también.

Esta imagen muestra el resultado de dos operaciones de cambio de tamaño. La captura de pantalla superior es un cambio de tamaño vertical al arrastrar el borde inferior de la ventana hacia abajo. La captura de pantalla inferior es un cambio de tamaño horizontal al arrastrar el borde derecho de la ventana hacia la derecha.

​          ![cambiar el tamaño de la aplicación AnchorPane](https://fxdocs.github.io/docs/html5/images/layout/anchorpaneapp_resize.png)         

​         Figura 41. "Aplicación AnchorPane redimensionada        

Los cuadros resaltados muestran que los controles que bordean `TextArea`conservan sus posiciones relativas a los bordes. El `TextArea`mismo se redimensiona en función del redimensionamiento de la ventana. Las compensaciones superior e inferior de la `TextArea`cuenta para los otros controles para que no se oculten.

```java
TextArea ta = new TextArea();

AnchorPane.setTopAnchor( ta, 40.0d );
AnchorPane.setBottomAnchor( ta, 40.0d );
AnchorPane.setRightAnchor( ta, 10.0d );
AnchorPane.setLeftAnchor( ta, 10.0d );

ap.getChildren().add( ta );
```

`AnchorPane`es una buena opción cuando tiene una mezcla de niños de tamaño variable y de posición fija. Se prefieren otros controles como `VBox`y `HBox`con una `Priority`configuración si solo hay un niño que necesita cambiar el tamaño. Utilice estos controles en lugar de `AnchorPane`con un solo niño que tenga las cuatro anclas configuradas. Recuerda que para establecer un ancla en un niño, usas un método estático de la clase contenedora como AnchorPane.setTopAnchor().

### Código completado

El siguiente es el código completo para el `AnchorPane`ejemplo.

```java
public class AnchorPaneApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        AnchorPane ap = new AnchorPane();

        // upper-right sign out control
        Hyperlink signoutLink = new Hyperlink("Sign Out");

        ap.getChildren().add( signoutLink );

        AnchorPane.setTopAnchor( signoutLink, 10.0d );
        AnchorPane.setRightAnchor( signoutLink, 10.0d );

        // lower-left status label
        Label statusLabel = new Label("Program status");
        ap.getChildren().add( statusLabel );

        AnchorPane.setBottomAnchor( statusLabel, 10.0d );
        AnchorPane.setLeftAnchor( statusLabel, 10.0d );

        // lower-right connection status control
        Circle circle = new Circle();
        circle.setFill(Color.GREEN );
        circle.setRadius(10);

        Label connLabel = new Label("Connection");

        HBox connHBox = new HBox();
        connHBox.setSpacing( 4.0d );
        connHBox.setAlignment(Pos.BOTTOM_RIGHT);
        connHBox.getChildren().addAll( connLabel, circle );

        AnchorPane.setBottomAnchor( connHBox, 10.0d );
        AnchorPane.setRightAnchor( connHBox, 10.0d );

        ap.getChildren().add( connHBox );

        // top-left content; takes up extra space
        TextArea ta = new TextArea();
        ap.getChildren().add( ta );

        AnchorPane.setTopAnchor( ta, 40.0d );
        AnchorPane.setBottomAnchor( ta, 40.0d );
        AnchorPane.setRightAnchor( ta, 10.0d );
        AnchorPane.setLeftAnchor( ta, 10.0d );

        Scene scene = new Scene(ap);

        primaryStage.setTitle("AnchorPaneApp");
        primaryStage.setScene( scene );
        primaryStage.setWidth(568);
        primaryStage.setHeight(320);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
```

## TilePane (panel de mosaico)

A `TilePane`se utiliza para el diseño de cuadrícula de celdas de igual tamaño. Las propiedades prefColumns y prefRows definen el número de filas y columnas en la cuadrícula. Para agregar nodos a `TilePane`, acceda a la propiedad child y llame al método add() o addAll(). Esto es más fácil de usar que `GridPane`lo que requiere una configuración explícita de la posición de fila/columna de los nodos.

Esta captura de pantalla muestra una `TilePane`cuadrícula definida como de tres por tres. El `TilePane`contiene nueve `Rectangle`objetos.

​         ![captura de pantalla de la aplicación tres por tres](https://fxdocs.github.io/docs/html5/images/layout/threebythreeapp_screenshot.png)        

A continuación se muestra el código completo para la cuadrícula de tres por tres. La propiedad children de `TilePane`proporciona el método addAll() al que `Rectangle`se agregan los objetos. La propiedad tileAlignment coloca cada uno de los `Rectangle`objetos en el centro de su mosaico correspondiente.

​        ThreeByThreeApp.java       

```java
public class ThreeByThreeApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        TilePane tilePane = new TilePane();
        tilePane.setPrefColumns(3);
        tilePane.setPrefRows(3);
        tilePane.setTileAlignment( Pos.CENTER );

        tilePane.getChildren().addAll(
                new Rectangle(50, 50, Color.RED),
                new Rectangle( 50, 50, Color.GREEN ),
                new Rectangle( 50, 50, Color.BLUE ),
                new Rectangle( 50, 50, Color.YELLOW ),
                new Rectangle( 50, 50, Color.CYAN ),
                new Rectangle( 50, 50, Color.PURPLE ),
                new Rectangle( 50, 50, Color.BROWN ),
                new Rectangle( 50, 50, Color.PINK ),
                new Rectangle( 50, 50, Color.ORANGE )
        );

        Scene scene = new Scene(tilePane);
        scene.setFill(Color.LIGHTGRAY);

        primaryStage.setTitle("3x3");
        primaryStage.setScene( scene );
        primaryStage.show();
    }

    public static void main(String[] args) {launch(args);}
}
```

Dado que todo el `Node`contenido de los `TilePane`Rectángulos era del mismo tamaño, el diseño está empaquetado y la configuración de TileAlignment no se nota. Cuando las propiedades tilePrefHeight y tilePrefWidth se configuran para que sean más grandes que el contenido, digamos mosaicos de 100x100 que contienen rectángulos de 50x50, tileAlignment determinará cómo se usará  el espacio adicional.

Consulte la siguiente clase ThreeByThreeApp modificada que establece el tilePrefHeight y el tilePrefWidth.

```java
        tilePane.setPrefTileHeight(100);
        tilePane.setPrefTileWidth(100);
```

![espacio de captura de pantalla de tres por tres aplicaciones](https://fxdocs.github.io/docs/html5/images/layout/threebythreeapp_screenshot_space.png)

En las capturas de pantalla anteriores, se proporcionaron nueve objetos Rectangle a la cuadrícula de tres por tres. Si el contenido no coincide con la `TilePane`definición, esas celdas colapsarán. Esta modificación agrega solo cinco Rectángulos en lugar de nueve. La primera fila contiene contenido para los tres mosaicos. La segunda fila tiene contenido solo para los dos primeros archivos. Falta la tercera fila por completo.

![captura de pantalla de tres por tres aplicación escasa](https://fxdocs.github.io/docs/html5/images/layout/threebythreeapp_screenshot_sparse.png)

Hay una propiedad de "orientación" que indica `TilePane`agregar elementos fila por fila (HORIZONTAL, el valor predeterminado) o columna por columna (VERTICAL). Si se usa VERTICAL, la primera columna tendrá tres elementos, la segunda columna tendrá solo los dos superiores y faltará la tercera columna. Esta captura de pantalla muestra los cinco rectángulos que se agregan a la cuadrícula de tres por tres (nueve mosaicos) con orientación VERTICAL.

![captura de pantalla de tres por tres aplicaciones](https://fxdocs.github.io/docs/html5/images/layout/threebythreeapp_screenshot_vert.png)

### Algoritmos

Es posible crear diseños de cuadrícula JavaFX con otros contenedores como `GridPane`, `VBox`y `HBox`. TilePane es una conveniencia que define el diseño de la cuadrícula de antemano y hace que agregar elementos a la cuadrícula sea una simple llamada add() o addAll(). A diferencia de un diseño de cuadrícula creado con una combinación de anidados `VBox`y `HBox`contenedores, los `TilePane`contenidos son elementos secundarios directos. Esto facilita el bucle sobre los niños durante el procesamiento de eventos, lo que ayuda a implementar ciertos algoritmos.

Esta aplicación de ejemplo coloca cuatro círculos en un archivo `TilePane`. Se adjunta un controlador de eventos `TilePane`que busca una selección de uno de los círculos. Si se selecciona un Círculo, se atenúa a través de la configuración de opacidad. Si se vuelve a seleccionar el Círculo, se restaura su color original. Esta captura de pantalla muestra la aplicación con el azul `Circle`que aparece de color púrpura porque se ha seleccionado.

![aplicación de azulejos](https://fxdocs.github.io/docs/html5/images/layout/tileapp.png)

El programa comienza agregando los elementos y configurando una propiedad personalizada "seleccionada" utilizando la API de flujo de Java 8.

​         TileApp.java        

```java
        TilePane tilePane = new TilePane();
        tilePane.setPrefColumns(2);
        tilePane.setPrefRows(2);
        tilePane.setTileAlignment( Pos.CENTER );

        Circle redCircle = new Circle(50, Color.RED);
        Circle greenCircle = new Circle( 50, Color.GREEN );
        Circle blueCircle = new Circle( 50, Color.BLUE );
        Circle yellowCircle = new Circle( 50, Color.YELLOW );

        List<Circle> circles = new ArrayList<>();
        circles.add( redCircle );
        circles.add( greenCircle );
        circles.add( blueCircle );
        circles.add( yellowCircle );

        circles
                .stream()
                .forEach( (c) -> c.getProperties().put( "selected", Boolean.FALSE ));

        tilePane.getChildren().addAll(
               circles
        );
```

A continuación, el controlador de eventos se adjunta al evento del mouse. Esto también está usando Java 8 Streams. El método filter() determina si `Circle`se selecciona o no usando el método Node.contains() en las coordenadas convertidas. Si esa expresión pasa, se usa findFirst() para recuperar la primera (y en este caso, la única) coincidencia. El bloque de código en ifPresent() establece el indicador "seleccionado" para realizar un seguimiento del `Circle`estado y ajusta la opacidad.

​         TileApp.java        

```java
    tilePane.setOnMouseClicked(

        (evt) -> tilePane
                    .getChildren()
                    .stream()
                    .filter( c ->
                        c.contains(
                          c.sceneToLocal(evt.getSceneX(), evt.getSceneY(), true)
                        )
                     )
                    .findFirst()
                    .ifPresent(
                            (c) -> {
                                Boolean selected = (Boolean) c.getProperties().get("selected");
                                if( selected == null || selected == Boolean.FALSE ) {
                                    c.setOpacity(0.3d);
                                    c.getProperties().put("selected", Boolean.TRUE);
                                } else {
                                    c.setOpacity( 1.0d );
                                    c.getProperties().put("selected", Boolean.FALSE);
                                }
                            }
                    )
    );
```

### otro controlador

Dado que el programa guarda los círculos en colecciones de Java `List`, el `TilePane`contenido se puede reemplazar con llamadas allAll() repetidas. Este controlador de eventos se activa cuando el usuario presiona una "S" en el archivo `Scene`. El contenido del respaldo `List`se mezcla y se vuelve a agregar al archivo `TilePane`.

​         TileApp.java        

```java
        scene.setOnKeyPressed(
                (evt) -> {
                    if( evt.getCode().equals(KeyCode.S) ) {
                        Collections.shuffle( circles );
                        tilePane.getChildren().clear();
                        tilePane.getChildren().addAll( circles );
                    }
                }
        );
```

Si bien es factible, una cuadrícula construida con VBoxes y HBoxes sería un poco más difícil debido a las estructuras anidadas. Además, `TilePane`no estirará el contenido para llenar espacio adicional, lo que lo hace adecuado para controles compuestos que deben empaquetarse juntos por razones ergonómicas.

`TilePane`crea un diseño basado en cuadrícula de celdas de igual tamaño. Los contenidos se agregan en `TilePane`función de la configuración de prefRows, prefColumns y orientación. Si la cuadrícula contiene más mosaicos que nodos agregados, habrá espacios en el diseño y las filas y columnas pueden contraerse si no se proporcionó contenido alguno. Esta publicación mostró un par de algoritmos que se implementaron fácilmente debido a la interfaz simple de TilePane.

### Código completo

A continuación se muestra el código completo de TileApp.

​         TileApp.java (completa)        

```java
public class TileApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        TilePane tilePane = new TilePane();
        tilePane.setPrefColumns(2);
        tilePane.setPrefRows(2);
        tilePane.setTileAlignment( Pos.CENTER );

        Circle redCircle = new Circle(50, Color.RED);
        Circle greenCircle = new Circle( 50, Color.GREEN );
        Circle blueCircle = new Circle( 50, Color.BLUE );
        Circle yellowCircle = new Circle( 50, Color.YELLOW );

        List<Circle> circles = new ArrayList<>();
        circles.add( redCircle );
        circles.add( greenCircle );
        circles.add( blueCircle );
        circles.add( yellowCircle );

        circles
                .stream()
                .forEach( (c) -> c.getProperties().put( "selected", Boolean.FALSE ));

        tilePane.getChildren().addAll(
               circles
        );

        tilePane.setOnMouseClicked(

            (evt) -> tilePane
                        .getChildren()
                        .stream()
                        .filter( c ->
                            c.contains(
                              c.sceneToLocal(evt.getSceneX(), evt.getSceneY(), true)
                            )
                         )
                        .findFirst()
                        .ifPresent(
                                (c) -> {
                                    Boolean selected = (Boolean) c.getProperties().get("selected");
                                    if( selected == null || selected == Boolean.FALSE ) {
                                        c.setOpacity(0.3d);
                                        c.getProperties().put("selected", Boolean.TRUE);
                                    } else {
                                        c.setOpacity( 1.0d );
                                        c.getProperties().put("selected", Boolean.FALSE);
                                    }
                                }
                        )
        );

        Scene scene = new Scene(tilePane);

        scene.setOnKeyPressed(
                (evt) -> {
                    if( evt.getCode().equals(KeyCode.S) ) {
                        Collections.shuffle( circles );
                        tilePane.getChildren().clear();
                        tilePane.getChildren().addAll( circles );
                    }
                }
        );

        primaryStage.setTitle("TileApp");
        primaryStage.setScene( scene );
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
```

## TitledPane

A `TitledPane`es un `Node`contenedor emparejado con a `Label`y un control opcional para mostrar y ocultar el contenido del contenedor. Dado que `TitledPane`está limitado a un solo `Node`, a menudo se combina con un contenedor que admite varios elementos secundarios como `VBox`. Funcionalmente, puede ocultar detalles no esenciales de un formulario o controles relacionados con grupos.

Este ejemplo es una aplicación de búsqueda web que acepta un conjunto de palabras clave en un archivo `TextField`. El usuario presiona el botón Buscar para ejecutar una búsqueda. El Avanzado `TitlePane`se expande para proporcionar argumentos de búsqueda adicionales.

Esta captura de pantalla muestra el estado no expandido que es la vista para un usuario que ejecuta una búsqueda simple de palabras clave.

​         ![titlepaneapp sin expandir](https://fxdocs.github.io/docs/html5/images/layout/titledpaneapp_unexpanded.png)        

​        Figura 47. TitledPane sin expandir       

La siguiente captura de pantalla muestra la vista para un usuario que requiere parámetros de búsqueda avanzada. El Advanced TitledPane se expandió presionando la flecha en el `TitledPane`encabezado.

​         ![titlepaneapp expandido](https://fxdocs.github.io/docs/html5/images/layout/titledpaneapp_expanded.png)        

Para crear un `TitledPane`, use el constructor para pasar un título de cadena y un solo `Node`hijo. También se puede usar el constructor predeterminado y el título y `Node`establecer usando setters. Este código usa el constructor parametrizado. A `VBox`es el único hijo de `TitledPane`. Sin embargo, el `VBox`mismo contiene varios controles.

​        TitledPaneApp.java       

```java
        VBox advancedVBox = new VBox(
                new Label("All Keywords"),
                new CheckBox(),
                new Label("Domains"),
                new TextField(),
                new Label("Time"),
                new ComboBox<>(
                    FXCollections.observableArrayList( "Day", "Month", "Year" )
                )
        );

        TitledPane titledPane = new TitledPane(
                "Advanced",
                advancedVBox
        );
        titledPane.setExpanded( false );
```

De forma predeterminada, `TitledPane`se expandirá. Esto no se ajusta al caso de uso de ocultar información no esencial, por lo que la propiedad expandida se establece después de que se crea el objeto.

### Plegable

Otra propiedad de `TitledPane`es plegable. De forma predeterminada, la `TitledPane`propiedad contraíble se establece en verdadero. Sin embargo, se puede proporcionar una agrupación rápida a los controles que no son plegables. La siguiente captura de pantalla muestra este caso de uso.

​          ![titlepaneapp no colapsable](https://fxdocs.github.io/docs/html5/images/layout/titledpaneapp_noncollapsible.png)         

​         Figura 49. Conjunto contraíble en falso        

Este código establece la bandera contraíble después de llamar al constructor.

```java
            VBox securityVBox = new VBox(
                    new Label("Owner"),
                    new TextField(),
                    new Label("Access Control"),
                    new TextField()
            );

            TitledPane tp = new TitledPane("Security", securityVBox);
            tp.setCollapsible( false );
```

### Código completo

El siguiente es el código completo para la primera demostración que involucra los parámetros de búsqueda ocultos "TitledPaneApp".

```java
public class TitledPaneApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        VBox vbox = new VBox(
                new Label("Keywords" ),
                new TextField()
        );

        vbox.setPadding( new Insets(10) );
        vbox.setSpacing( 10 );

        VBox advancedVBox = new VBox(
                new Label("All Keywords"),
                new CheckBox(),
                new Label("Domains"),
                new TextField(),
                new Label("Time"),
                new ComboBox<>(
                    FXCollections.observableArrayList( "Day", "Month", "Year" )
                )
        );

        TitledPane titledPane = new TitledPane(
                "Advanced",
                advancedVBox
        );
        titledPane.setExpanded( false );

        vbox.getChildren().addAll(
                titledPane,
                new Button("Search")
        );

        Scene scene = new Scene( vbox );

        primaryStage.setTitle( "TitledPaneApp" );
        primaryStage.setScene( scene );
        primaryStage.setWidth( 568 );
        primaryStage.setHeight( 320 );
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
```

# Estructura de la aplicación

## El patrón MVVM

**Modelo-vista-controlador** (**MVC**) es un patrón de arquitectura de software, que separa los datos y principalmente lo que es la lógica de negocio de una aplicación de su representación y el módulo encargado de  gestionar los eventos y las comunicaciones. Para ello MVC propone la  construcción de tres componentes distintos que son el **modelo**, la **vista** y el **controlador**, es decir, por un lado define componentes para la representación de la  información, y por otro lado para la interacción del usuario. Este patrón de arquitectura de software se basa en las ideas de reutilización de código y la separación de conceptos, características que buscan facilitar la tarea de desarrollo de aplicaciones y su posterior mantenimiento.

De manera genérica, los componentes de MVC se podrían definir como sigue:

- El **Modelo**: Es la representación de la información con la  cual el sistema opera, por lo tanto gestiona todos los accesos a dicha  información, tanto consultas como actualizaciones, implementando también los privilegios de acceso que se hayan descrito en las especificaciones de la aplicación (lógica de negocio). Envía a la &apos;vista&apos; aquella parte de la información que en cada momento  se le solicita para que sea mostrada (típicamente a un usuario). Las  peticiones de acceso o manipulación de información llegan al &apos;modelo&apos; a  través del &apos;controlador&apos;.
- El **Controlador**: Responde a eventos (usualmente acciones del  usuario) e invoca peticiones al &apos;modelo&apos; cuando se hace alguna solicitud sobre la información (por ejemplo, editar un documento o un registro en una base de datos). También puede enviar comandos a su &apos;vista&apos; asociada si se solicita un cambio en la forma en que se presenta el &apos;modelo&apos;  (por ejemplo, desplazamiento o scroll por un documento o por los  diferentes registros de una base de datos), por tanto se podría decir  que el &apos;controlador&apos; hace de intermediario entre la &apos;vista&apos; y el  &apos;modelo&apos; (véase ***[Middleware](https://es.wikipedia.org/wiki/Middleware)***).
- La **Vista**: Presenta el &apos;modelo&apos; (información y *lógica de negocio*) en un formato adecuado para interactuar (usualmente la interfaz de usuario), por tanto requiere de dicho &apos;modelo&apos; la información que debe representar como salida.

<img src="/assets/MVC-Process.png" style="zoom:50%;" />

## Scene Builder

Scene Builder es una alternativa orientada al diseño que puede ser más productiva. Además es multiplataforma y está disponible para GNU/Linux, Windows y Mac. Scene Builder funciona con el ecosistema JavaFX: controles oficiales, proyectos comunitarios y ofertas de Gluon que incluyen [Gluon Mobile](https://gluonhq.com/products/mobile), [Gluon Desktop](https://gluonhq.com/products /escritorio) y [Gluon CloudLink](https://gluonhq.com/products/cloudlink).

El diseño de la interfaz de usuario *drag&amp;drop* permite una iteración rápida. La separación de los archivos de diseño y lógica permite que los miembros del equipo se concentren rápida y fácilmente en su capa específica de desarrollo de aplicaciones.

Scene Builder es gratuito y de código abierto, pero cuenta con el respaldo de Gluon. Están disponibles [ofertas de soporte comercial](https://gluonhq.com/services), que incluyen [formación](https://gluonhq.com/services/training) y [servicios de consultoría personalizados](https://gluonhq.com /servicios/consultoría).

Descarga e información: https://gluonhq.com/products/scene-builder/

<img src="/assets/SceneBuilder1.png" style="zoom: 50%;" />

<img src="/assets/SceneBuilder2.png" style="zoom:50%;" />

# Mejores prácticas

## Propiedades estilizables

Se puede diseñar una propiedad JavaFX a través de css usando `StyleableProperty`. Esto es útil cuando los controles necesitan propiedades que se pueden configurar a través de css.

Para usar `StyleableProperty` en un Control, se necesita crear un nuevo `CssMetaData` usando `StyleableProperty`. Los `CssMetaData` creados para un control deben agregarse a `List<CssMetaData>` obtenidos del antecesor del control. Esta nueva lista luego se devuelve desde el archivo `getControlCssMetaData()`.

Por convención, las clases de control que tienen `CssMetaData` implementarán un método estático `getClassCssMetaData()` y es habitual que `getControlCssMetaData()` simplemente devuelva `getClassCssMetaData()`. El propósito de `getClassCssMetaData()` es permitir que las subclases incluyan fácilmente los `CssMetaData` de algún antepasado.

```java
// StyleableProperty
private final StyleableProperty<Color> color =
    new SimpleStyleableObjectProperty<>(COLOR, this, "color");

// Typical JavaFX property implementation
public Color getColor() {
    return this.color.getValue();
}
public void setColor(final Color color) {
    this.color.setValue(color);
}
public ObjectProperty<Color> colorProperty() {
    return (ObjectProperty<Color>) this.color;
}

// CssMetaData
private static final CssMetaData<MY_CTRL, Paint> COLOR =
    new CssMetaData<MY_CTRL, Paint>("-color", PaintConverter.getInstance(), Color.RED) {

    @Override
    public boolean isSettable(MY_CTRL node) {
        return node.color == null || !node.color.isBound();
    }

    @Override
    public StyleableProperty<Paint> getStyleableProperty(MY_CTRL node) {
        return node.color;
    }
};

private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;
static {
    // Fetch CssMetaData from its ancestors
    final List<CssMetaData<? extends Styleable, ?>> styleables =
        new ArrayList<>(Control.getClassCssMetaData());
    // Add new CssMetaData
    styleables.add(COLOR);
    STYLEABLES = Collections.unmodifiableList(styleables);
}

// Return all CssMetadata information
public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
    return STYLEABLES;
}

@Override
public List<CssMetaData<? extends Styleable, ?>> getControlCssMetaData() {
    return getClassCssMetaData();
}
```

La creación de `StyleableProperty` y `CssMetaData` necesita una gran cantidad de código repetitivo y esto se puede reducir mediante el uso de `StyleablePropertyFactory` . `StyleablePropertyFactory` contiene métodos para crear `StyleableProperty` con los `CssMetaData` correspondientes.

```java
// StyleableProperty
private final StyleableProperty<Color> color =
    new SimpleStyleableObjectProperty<>(COLOR, this, "color");

// Typical JavaFX property implementation
public Color getColor() {
    return this.color.getValue();
}
public void setColor(final Color color) {
    this.color.setValue(color);
}
public ObjectProperty<Color> colorProperty() {
    return (ObjectProperty<Color>) this.color;
}

// StyleablePropertyFactory
private static final StyleablePropertyFactory<MY_CTRL> FACTORY =
    new StyleablePropertyFactory<>(Control.getClassCssMetaData());

// CssMetaData from StyleablePropertyFactory
private static final CssMetaData<MY_CTRL, Color> COLOR =
    FACTORY.createColorCssMetaData("-color", s -> s.color, Color.RED, false); 

// Return all CssMetadata information from StyleablePropertyFactory
public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
    return FACTORY.getCssMetaData();
}

@Override public List<CssMetaData<? extends Styleable, ?>> getControlCssMetaData() {
    return getClassCssMetaData();
}
```

## Tareas

Ahora veremos cómo usar una tarea JavaFX para mantener la IU *responsible*. Es imperativo que cualquier operación que tarde más de unos pocos cientos de milisegundos se ejecute en un subproceso separado para evitar bloquear la interfaz de usuario. Una tarea concluye la secuencia de pasos en una operación de larga duración y proporciona devoluciones de llamada para los posibles resultados.

La clase **Task** también mantiene al usuario al tanto de la operación a través de propiedades que se pueden vincular a controles de interfaz de usuario como `ProgressBars` y `Labels`. El enlace actualiza dinámicamente la interfaz de usuario. Estas propiedades incluyen

1. **`runningProperty`** : si la tarea se está ejecutando o no
2. **`ProgressProperty`** : el porcentaje completado de una operación.
3. **`messageProperty`** : texto que describe un paso en la operación

### Demostración

Las siguientes capturas de pantalla muestran el funcionamiento de una aplicación de recuperación de HTML.

Ingresar una URL y presionar "Ir" iniciará una tarea JavaFX. Al ejecutarse, la tarea hará visible un `HBox` que contiene una barra de progreso y una etiqueta. `ProgressBar` y `Label` se actualizan a lo largo de la operación.

​          ![tareas pb](https://fxdocs.github.io/docs/html5/images/best-practices/tasks_pb.png)         

Cuando finaliza la recuperación, se invoca al metodo `succeeded()` y se actualiza la interfaz de usuario. Tenga en cuenta que la llamada a `succeeded()` se lleva a cabo en el subproceso FX, por lo que es seguro manipular los controles.

​          ![contenido de las tareas](https://fxdocs.github.io/docs/html5/images/best-practices/tasks_contents.png)         

Si hubo un error al recuperar el HTML, se invoca a `failed()` y se muestra una alerta de error. `failed()` también tiene lugar en el subproceso FX. Esta captura de pantalla muestra una entrada no válida. Se usa una "h" en la URL en lugar de "http".

​          ![error de tareas](https://fxdocs.github.io/docs/html5/images/best-practices/tasks_error.png)         

### Código

Se coloca un controlador de eventos en el botón Obtener HTML que crea la tarea. El punto de entrada de la Tarea es el método call() que comienza llamando a updateMessage() y updateProgress(). Estos métodos se ejecutan en el subproceso FX y generarán actualizaciones en cualquier propiedad enlazada.

El programa continúa emitiendo un HTTP GET usando clases estándar de java.net. Se crea una cadena "retval" a partir de los caracteres recuperados. Las propiedades de mensaje y progreso se actualizan con más llamadas a updateMessage() y updateProgress(). El método call() finaliza con la devolución de la cadena que contiene el texto HTML.

En una operación exitosa, se invoca la devolución de llamada de éxito (). getValue() es un método de tarea que devolverá el valor acumulado en la tarea (recuerde "retval"). El tipo del valor es lo que se proporciona en el argumento genérico, en este caso "String". Esto podría ser un tipo complejo como un objeto de dominio o una colección. La operación de éxito () se ejecuta en el subproceso FX, por lo que la cadena getValue () se establece directamente en el área de texto.

Si la operación falla, se lanza una excepción. La excepción es capturada por la tarea y convertida en una llamada fallida(). fail() también es seguro para subprocesos FX y muestra una alerta.

```java
String url = tfURL.getText();

Task<String> task = new Task<String>() {

    @Override
    protected String call() throws Exception {

        updateMessage("Getting HTML from " + url );
        updateProgress( 0.5d, 1.0d );

        HttpURLConnection c = null;
        InputStream is = null;
        String retval = "";

        try {

            c = (HttpURLConnection) new URL(url).openConnection();

            updateProgress( 0.6d, 1.0d );
            is = c.getInputStream();
            int ch;
            while( (ch=is.read()) != -1 ) {
                retval += (char)ch;
            }

        } finally {
            if( is != null ) {
                is.close();
            }
            if( c != null ) {
                c.disconnect();
            }
        }

        updateMessage("HTML retrieved");
        updateProgress( 1.0d, 1.0d );

        return retval;
    }

    @Override
    protected void succeeded() {
        contents.setText( getValue() );
    }

    @Override
    protected void failed() {
        Alert alert = new Alert(Alert.AlertType.ERROR, getException().getMessage() );
        alert.showAndWait();
    }
};
```

Tenga en cuenta que la tarea no actualiza la barra de progreso y la etiqueta de estado directamente. En su lugar, Task realiza llamadas seguras a updateMessage() y updateProgress(). Para actualizar la interfaz de usuario, se utiliza el enlace JavaFX en las siguientes declaraciones.

```java
bottomControls.visibleProperty().bind( task.runningProperty() );
pb.progressProperty().bind( task.progressProperty() );
messageLabel.textProperty().bind( task.messageProperty() );
```

Task.runningProperty es un valor booleano que se puede vincular a bottomControls HBox visibleProperty. Task.progressProperty es un doble que se puede vincular a ProgressBarprogressProperty. Task.messageProperty es una cadena que se puede vincular a la etiqueta de estado textProperty.

Para ejecutar la tarea, cree un subproceso que proporcione la tarea como argumento del constructor e invoque start().

```java
new Thread(task).start();
```

Para cualquier operación de ejecución prolongada (archivo IO, la red), use una tarea JavaFX para mantener la capacidad de respuesta de su aplicación. La tarea JavaFX le brinda a su aplicación una forma consistente de manejar operaciones asincrónicas y expone varias propiedades que se pueden usar para eliminar la lógica repetitiva y de programación.

### Código completo

El código se puede probar en un solo archivo .java.

```java
public class ProgressBarApp extends Application {

    private HBox bottomControls;
    private ProgressBar pb;
    private Label messageLabel;

    private TextField tfURL;

    private TextArea contents;

    @Override
    public void start(Stage primaryStage) throws Exception {

        Parent p = createMainView();

        Scene scene = new Scene(p);

        primaryStage.setTitle("ProgressBarApp");
        primaryStage.setWidth( 667 );
        primaryStage.setHeight( 376 );
        primaryStage.setScene( scene );
        primaryStage.show();
    }

    private Parent createMainView() {

        VBox vbox = new VBox();
        vbox.setPadding( new Insets(10) );
        vbox.setSpacing( 10 );

        HBox topControls = new HBox();
        topControls.setAlignment(Pos.CENTER_LEFT);
        topControls.setSpacing( 4 );

        Label label = new Label("URL");
        tfURL = new TextField();
        HBox.setHgrow( tfURL, Priority.ALWAYS );
        Button btnGetHTML = new Button("Get HTML");
        btnGetHTML.setOnAction( this::getHTML );
        topControls.getChildren().addAll(label, tfURL, btnGetHTML);

        contents = new TextArea();
        VBox.setVgrow( contents, Priority.ALWAYS );

        bottomControls = new HBox();
        bottomControls.setVisible(false);
        bottomControls.setSpacing( 4 );
        HBox.setMargin( bottomControls, new Insets(4));

        pb = new ProgressBar();
        messageLabel = new Label("");
        bottomControls.getChildren().addAll(pb, messageLabel);

        vbox.getChildren().addAll(topControls, contents, bottomControls);

        return vbox;
    }

    public void getHTML(ActionEvent evt) {

        String url = tfURL.getText();

        Task<String> task = new Task<String>() {

            @Override
            protected String call() throws Exception {

                updateMessage("Getting HTML from " + url );
                updateProgress( 0.5d, 1.0d );

                HttpURLConnection c = null;
                InputStream is = null;
                String retval = "";

                try {

                    c = (HttpURLConnection) new URL(url).openConnection();

                    updateProgress( 0.6d, 1.0d );
                    is = c.getInputStream();
                    int ch;
                    while( (ch=is.read()) != -1 ) {
                        retval += (char)ch;
                    }

                } finally {
                    if( is != null ) {
                        is.close();
                    }
                    if( c != null ) {
                        c.disconnect();
                    }
                }

                updateMessage("HTML retrieved");
                updateProgress( 1.0d, 1.0d );

                return retval;
            }

            @Override
            protected void succeeded() {
                contents.setText( getValue() );
            }

            @Override
            protected void failed() {
                Alert alert = new Alert(Alert.AlertType.ERROR, getException().getMessage() );
                alert.showAndWait();
            }
        };

        bottomControls.visibleProperty().bind( task.runningProperty() );
        pb.progressProperty().bind( task.progressProperty() );
        messageLabel.textProperty().bind( task.messageProperty() );

        new Thread(task).start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
```

## Evitar Nulos en ComboBoxes

Para usar a `ComboBox`en JavaFX, declare una Lista de elementos y establezca un valor inicial usando setValue(). El `ComboBox`método getValue() recupera el valor seleccionado actualmente. Si no se proporciona un valor inicial, el control tiene un valor nulo predeterminado.

El valor nulo es un problema cuando `ComboBox`impulsa otra lógica como una transformación a mayúsculas o la búsqueda de un registro de base de datos. Si bien generalmente se usa una verificación nula para evitar este tipo de error, se prefiere un objeto vacío para simplificar el código. Los cuadros combinados a menudo aparecen en grupos y la técnica de objetos vacíos reduce las comprobaciones nulas en la interacción de los cuadros combinados relacionados y en las operaciones de guardar y cargar.

Este artículo presenta un par de ComboBoxes relacionados. Una selección de país en uno `ComboBox`modifica la lista de elementos de ciudad disponibles en un segundo `ComboBox`. No se requiere ninguna selección. El usuario puede presionar Guardar `Button`en cualquier momento y, si no se realiza ninguna selección `ComboBox`, se devolverá un objeto vacío, en este caso una Cadena vacía.

Esta es una captura de pantalla de la aplicación. Si selecciona "Suiza" de un valor inicial vacío, la ciudad se llenará `ComboBox`de ciudades suizas. Seleccionando la ciudad "Zurich" y presionando Guardar recuperará esos valores.

​         ![captura de pantalla de combo no nulo](https://fxdocs.github.io/docs/html5/images/best-practices/nonullcombo_screenshot.png)        

​        Figura 64. Cuadros combinados relacionados       

### Estructura de datos

Las estructuras de datos que soportan la aplicación son una Lista de países y un Mapa de ciudades. El Mapa de ciudades utiliza el país como clave.

​         NoNullComboApp.clase        

```java
public class NoNullComboApp extends Application {

    private List<String> countries = new ArrayList<>();

    private Map<String, List<String>> citiesMap = new LinkedHashMap<>();

    private void initData() {

        String COUNTRY_FR = "France";
        String COUNTRY_DE = "Germany";
        String COUNTRY_CH = "Switzerland";

        countries.add(COUNTRY_FR); countries.add(COUNTRY_DE); countries.add(COUNTRY_CH);

        List<String> frenchCities = new ArrayList<>();
        frenchCities.add("Paris");
        frenchCities.add("Strasbourg");

        List<String> germanCities = new ArrayList<>();
        germanCities.add("Berlin");
        germanCities.add("Cologne");
        germanCities.add("Munich");

        List<String> swissCities = new ArrayList<>();
        swissCities.add("Zurich");

        citiesMap.put(COUNTRY_FR, frenchCities );
        citiesMap.put(COUNTRY_DE, germanCities );
        citiesMap.put(COUNTRY_CH, swissCities );
    }
```

Para recuperar el conjunto de ciudades de un país determinado, utilice el método get() del Mapa. El método containsKey() se puede utilizar para determinar si el mapa contiene o no un valor para el país especificado. En este ejemplo, containsKey() se usará para manejar el caso del objeto vacío.

### interfaz de usuario

La interfaz de usuario es un par de cuadros combinados con etiquetas y un botón Guardar. Los controles se colocan en a `VBox`y justificados a la izquierda. El `VBox`está envuelto en un `TilePane`y centrado. Se `TilePane`utilizó ya que no se estira `VBox`horizontalmente.

​         NoNullComboApp.clase        

```java
    @Override
    public void start(Stage primaryStage) throws Exception {

        Label countryLabel = new Label("Country:");
        country.setPrefWidth(200.0d);
        Label cityLabel = new Label("City:");
        city.setPrefWidth(200.0d);
        Button saveButton = new Button("Save");

        VBox vbox = new VBox(
                countryLabel,
                country,
                cityLabel,
                city,
                saveButton
        );
        vbox.setAlignment(Pos.CENTER_LEFT );
        vbox.setSpacing( 10.0d );

        TilePane outerBox = new TilePane(vbox);
        outerBox.setAlignment(Pos.CENTER);

        Scene scene = new Scene(outerBox);

        initData();
```

### Valores iniciales

Como se mencionó anteriormente, si no se especifica un valor para un `ComboBox`, se devolverá un valor nulo en una llamada a getValue(). Aunque existen varias técnicas defensivas (si se verifica, métodos Commons StringUtils) para defenderse de NullPointerExceptions, es mejor evitarlas por completo. Esto es especialmente cierto cuando las interacciones se vuelven complejas o hay varios ComboBoxes que permiten selecciones vacías.

​         NoNullComboApp.clase        

```java
        country.getItems().add("");
        country.getItems().addAll( countries );
        country.setValue( "" );  // empty selection is object and not null

        city.getItems().add("");
        city.setValue( "" );
```

En esta aplicación, el país `ComboBox`no se cambiará, por lo que sus elementos se agregan en el método start(). El país comienza con una selección inicial vacía al igual que la ciudad. Ciudad, en este punto, contiene un único elemento vacío.

### Interacción

Cuando se cambia el valor del país, se `ComboBox`debe reemplazar el contenido de la ciudad. Es común usar clear() en la lista de respaldo; sin embargo, esto producirá un valor nulo en `ComboBox`(sin elementos, sin valor). En su lugar, use removeIf() con una cláusula para mantener un único elemento vacío. Con la lista limpia de todos los datos (excepto el elemento vacío), los contenidos recién seleccionados se pueden agregar con addAll().

​         NoNullComboApp.clase        

```java
        country.setOnAction( (evt) -> {

            String cty = country.getValue();

            city.getItems().removeIf( (c) -> !c.isEmpty() );

            if( citiesMap.containsKey(cty) ) {  // not an empty key
                city.getItems().addAll( citiesMap.get(cty) );
            }
        });

        saveButton.setOnAction( (evt) -> {
           System.out.println("saving country=&apos;" + country.getValue() +
                                      "&apos;, city=&apos;" + city.getValue() + "&apos;");
        });
```

La acción del botón Guardar imprimirá los valores. En ningún caso se devolverá un valor nulo desde getValue().

Si es un desarrollador de Java, ha escrito "si no es nulo" miles de veces. Sin embargo, proyecto tras proyecto, veo NullPointerExceptions que resaltan los casos que se perdieron o las nuevas condiciones que surgieron. Este artículo presentó una técnica para mantener objetos vacíos en ComboBoxes estableciendo un valor inicial y usando removeIf() en lugar de clear() al cambiar listas. Aunque este ejemplo usó objetos String, esto se puede expandir para trabajar con objetos de dominio que tienen una implementación hashCode/equals,  una representación de objeto vacía y cellFactory o toString() para producir una vista vacía.

### Código completo

El código se puede probar en un solo archivo .java.

​         NoNullComboApp.clase        

```java
public class NoNullComboApp extends Application {

    private final ComboBox<String> country = new ComboBox<>();
    private final ComboBox<String> city = new ComboBox<>();

    private List<String> countries = new ArrayList<>();

    private Map<String, List<String>> citiesMap = new LinkedHashMap<>();

    @Override
    public void start(Stage primaryStage) throws Exception {

        Label countryLabel = new Label("Country:");
        country.setPrefWidth(200.0d);
        Label cityLabel = new Label("City:");
        city.setPrefWidth(200.0d);
        Button saveButton = new Button("Save");

        VBox vbox = new VBox(
                countryLabel,
                country,
                cityLabel,
                city,
                saveButton
        );
        vbox.setAlignment(Pos.CENTER_LEFT );
        vbox.setSpacing( 10.0d );

        TilePane outerBox = new TilePane(vbox);
        outerBox.setAlignment(Pos.CENTER);

        Scene scene = new Scene(outerBox);

        initData();

        country.getItems().add("");
        country.getItems().addAll( countries );
        country.setValue( "" );  // empty selection is object and not null

        city.getItems().add("");
        city.setValue( "" );

        country.setOnAction( (evt) -> {

            String cty = country.getValue();

            city.getItems().removeIf( (c) -> !c.isEmpty() );

            if( citiesMap.containsKey(cty) ) {  // not an empty key
                city.getItems().addAll( citiesMap.get(cty) );
            }
        });

        saveButton.setOnAction( (evt) -> {
           System.out.println("saving country=&apos;" + country.getValue() +
                                      "&apos;, city=&apos;" + city.getValue() + "&apos;");
        });

        primaryStage.setTitle("NoNullComboApp");
        primaryStage.setScene( scene );
        primaryStage.setWidth( 320 );
        primaryStage.setHeight( 480 );
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void initData() {

        String COUNTRY_FR = "France";
        String COUNTRY_DE = "Germany";
        String COUNTRY_CH = "Switzerland";

        countries.add(COUNTRY_FR); countries.add(COUNTRY_DE); countries.add(COUNTRY_CH);

        List<String> frenchCities = new ArrayList<>();
        frenchCities.add("Paris");
        frenchCities.add("Strasbourg");

        List<String> germanCities = new ArrayList<>();
        germanCities.add("Berlin");
        germanCities.add("Cologne");
        germanCities.add("Munich");

        List<String> swissCities = new ArrayList<>();
        swissCities.add("Zurich");

        citiesMap.put(COUNTRY_FR, frenchCities );
        citiesMap.put(COUNTRY_DE, germanCities );
        citiesMap.put(COUNTRY_CH, swissCities );
    }
}
```

# Píldoras informáticas relacionadas

- https://www.youtube.com/playlist?list=PLNjWMbvTJAIjLRW2qyuc4DEgFVW5YFRSR
- https://www.youtube.com/playlist?list=PLaxZkGlLWHGUWZxuadN3J7KKaICRlhz5-

# Fuentes de información

- [Wikipedia](https://es.wikipedia.org)
- [Programación (Grado Superior) - Juan Carlos Moreno Pérez (Ed. Ra-ma)](https://www.ra-ma.es/libro/programacion-grado-superior_48302/)
- Apuntes IES Henri Matisse (Javi García Jimenez?)
- Apuntes AulaCampus
- [Apuntes José Luis Comesaña](https://www.sitiolibre.com/)
- [Apuntes IOC Programació bàsica (Joan Arnedo Moreno)](https://ioc.xtec.cat/materials/FP/Recursos/fp_asx_m03_/web/fp_asx_m03_htmlindex/index.html)
- [Apuntes IOC Programació Orientada a Objectes (Joan Arnedo Moreno)](https://ioc.xtec.cat/materials/FP/Recursos/fp_dam_m03_/web/fp_dam_m03_htmlindex/index.html)
- [FXDocs](https://github.com/FXDocs/docs)
- https://openjfx.io/openjfx-docs/

