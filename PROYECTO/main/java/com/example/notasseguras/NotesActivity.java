// Paquete base de la app; debe coincidir con el applicationId y estructura de carpetas
package com.example.notasseguras;

// Importaciones de Android usadas en esta Activity
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

// Importaciones de AndroidX y Material necesarias
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.cardview.widget.CardView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

// Activity que muestra y gestiona una lista de notas en un RecyclerView
public class NotesActivity extends AppCompatActivity {

    // Referencias a vistas del layout activity_notes.xml
    private RecyclerView rvNotes;              // Lista de notas
    private FloatingActionButton fabAdd;       // Botón flotante para crear una nueva nota
    private TextView tvEmptyState;             // Texto que se muestra cuando no hay notas

    // Fuente de datos en memoria (lista simple de notas para demo)
    private final List<Note> notes = new ArrayList<>();
    private NotesAdapter adapter;              // Adaptador que "pinta" las notas en el RecyclerView

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Vincula esta Activity con el XML de interfaz (debe existir activity_notes.xml)
        setContentView(R.layout.activity_notes);

        // Configuración de la toolbar (opcional). Si existe en el layout, la usamos como ActionBar
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

        // Obtención de referencias a las vistas declaradas en el XML
        rvNotes = findViewById(R.id.rvNotes);
        fabAdd = findViewById(R.id.fabAdd);
        tvEmptyState = findViewById(R.id.tvEmptyState);

        // El RecyclerView necesita un LayoutManager; LinearLayoutManager lo muestra en lista vertical
        rvNotes.setLayoutManager(new LinearLayoutManager(this));

        // Creamos el adaptador, pasando la lista de datos y los callbacks de interacción
        adapter = new NotesAdapter(notes, new OnNoteInteraction() {
            @Override
            public void onClick(int position) {
                // Click corto: editar la nota existente en 'position'
                openNoteDialog(notes.get(position), position);
            }

            @Override
            public void onLongClick(int position) {
                // Click largo: pedir confirmación y eliminar la nota
                confirmDelete(position);
            }
        });

        // Conectamos el adaptador al RecyclerView
        rvNotes.setAdapter(adapter);

        // Acción del FAB: abrir el diálogo para crear una nota nueva (existing = null)
        fabAdd.setOnClickListener(v -> openNoteDialog(null, -1));

        // Actualizamos el estado vacío (mostrar/ocultar tvEmptyState)
        updateEmptyState();
    }

    // Muestra el texto de "sin notas" cuando la lista está vacía; lo oculta si hay elementos
    private void updateEmptyState() {
        if (tvEmptyState == null) return;
        tvEmptyState.setVisibility(notes.isEmpty() ? View.VISIBLE : View.GONE);
    }

    // Diálogo de confirmación para eliminar una nota en 'position'
    private void confirmDelete(int position) {
        new AlertDialog.Builder(this)
                .setTitle("Eliminar nota")
                .setMessage("¿Seguro que deseas eliminar esta nota?")
                .setPositiveButton("Eliminar", (d, w) -> {
                    // Eliminamos del dataset y notificamos al adaptador el cambio puntual
                    notes.remove(position);
                    adapter.notifyItemRemoved(position);
                    updateEmptyState();
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    // ------------------------------------------------------------
    // Diálogo para crear/editar una nota.
    // Si 'existing' es null: crea. Si no: edita la nota existente.
    // ------------------------------------------------------------
    private void openNoteDialog(Note existing, int positionToEdit) {
        boolean isEditing = existing != null; // Flag para saber si editamos o creamos

        // Contenedor vertical para apilar Título y Contenido dentro del diálogo
        LinearLayout container = new LinearLayout(this);
        container.setOrientation(LinearLayout.VERTICAL);
        int pad = dp(16); // Conversión de 16dp a px
        container.setPadding(pad, pad, pad, pad);

        // Campo de entrada para el Título de la nota
        EditText etTitle = new EditText(this);
        etTitle.setHint("Título");
        etTitle.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        etTitle.setSingleLine(true);
        etTitle.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        container.addView(etTitle);

        // Pequeño espacio entre campos
        addSpacer(container, 8);

        // Campo de entrada para el Contenido de la nota
        EditText etContent = new EditText(this);
        etContent.setHint("Contenido de la nota");
        etContent.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        etContent.setMinLines(4);                       // Altura mínima (para que parezca un área de texto)
        etContent.setGravity(Gravity.TOP | Gravity.START); // Texto inicia arriba-izquierda
        etContent.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        container.addView(etContent);

        // Si estamos editando, precargar los valores existentes
        if (isEditing) {
            etTitle.setText(existing.title);
            etContent.setText(existing.content);
        }

        // Construcción del AlertDialog con el formulario
        new AlertDialog.Builder(this)
                .setTitle(isEditing ? "Editar nota" : "Nueva nota")
                .setView(container) // Inyectamos nuestro layout construido por código
                .setPositiveButton("Guardar", (dialog, which) -> {
                    // Al guardar, tomar los valores escritos
                    String title = etTitle.getText().toString().trim();
                    String content = etContent.getText().toString().trim();

                    // Si ambos campos están vacíos, no hacemos nada
                    if (TextUtils.isEmpty(title) && TextUtils.isEmpty(content)) {
                        return;
                    }

                    if (isEditing) {
                        // Actualizamos la nota existente y notificamos cambio puntual
                        existing.title = title;
                        existing.content = content;
                        adapter.notifyItemChanged(positionToEdit);
                    } else {
                        // Insertamos la nueva nota al inicio de la lista
                        notes.add(0, new Note(title, content));
                        adapter.notifyItemInserted(0);
                        rvNotes.scrollToPosition(0); // Hacer scroll a la nueva nota
                    }
                    updateEmptyState();
                })
                .setNegativeButton("Cancelar", null) // Cierra el diálogo sin cambios
                .show();
    }

    // Añade un espacio vertical (altura en dp) dentro de un LinearLayout
    private void addSpacer(LinearLayout parent, int dp) {
        View spacer = new View(this);
        spacer.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, dp(dp)));
        parent.addView(spacer);
    }

    // Conversión de valores dp a px según la densidad de la pantalla
    private int dp(int value) {
        return Math.round(getResources().getDisplayMetrics().density * value);
    }

    // -------------------------------
    // Modelo simple de Nota (POJO)
    // -------------------------------
    static class Note {
        String title;    // Título de la nota
        String content;  // Contenido/cuerpo de la nota

        Note(String t, String c) {
            this.title = t;
            this.content = c;
        }
    }

    // -------------------------------
    // Interfaz para manejar clics en la lista
    // -------------------------------
    interface OnNoteInteraction {
        void onClick(int position);     // Click corto: editar
        void onLongClick(int position); // Click largo: eliminar
    }

    // --------------------------------------------------------
    // Adapter del RecyclerView: crea y une vistas de cada nota
    // --------------------------------------------------------
    static class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteVH> {

        private final List<Note> data;            // Fuente de datos a renderizar
        private final OnNoteInteraction listener; // Callbacks para eventos de clic

        NotesAdapter(List<Note> data, OnNoteInteraction listener) {
            this.data = data;
            this.listener = listener;
        }

        @NonNull
        @Override
        public NoteVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            // Creamos una CardView por código para no depender de un XML de item
            CardView card = new CardView(parent.getContext());

            // LayoutParams para que la tarjeta ocupe ancho completo y alto ajustado al contenido
            RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            // Margen vertical de 8dp entre tarjetas
            int m = Math.round(parent.getResources().getDisplayMetrics().density * 8);
            lp.setMargins(0, m, 0, m);
            card.setLayoutParams(lp);

            // Estética de la CardView (padding, radio de esquinas, elevación/sombra)
            card.setUseCompatPadding(true);
            card.setRadius(Math.round(parent.getResources().getDisplayMetrics().density * 16));
            card.setCardElevation(Math.round(parent.getResources().getDisplayMetrics().density * 4));

            // Contenedor interno vertical de la tarjeta
            LinearLayout inner = new LinearLayout(parent.getContext());
            inner.setOrientation(LinearLayout.VERTICAL);
            int pad = Math.round(parent.getResources().getDisplayMetrics().density * 14);
            inner.setPadding(pad, pad, pad, pad);
            card.addView(inner);

            // TextView para el título (negritas, 1 línea, elipsis al final)
            TextView tvTitle = new TextView(parent.getContext());
            tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            tvTitle.setEllipsize(TextUtils.TruncateAt.END);
            tvTitle.setMaxLines(1);
            tvTitle.setTypeface(tvTitle.getTypeface(), android.graphics.Typeface.BOLD);
            inner.addView(tvTitle);

            // Espaciador entre título y contenido
            View spacer = new View(parent.getContext());
            spacer.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    Math.round(parent.getResources().getDisplayMetrics().density * 6)));
            inner.addView(spacer);

            // TextView para el contenido (gris oscuro, hasta 3 líneas con elipsis)
            TextView tvContent = new TextView(parent.getContext());
            tvContent.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            tvContent.setTextColor(0xFF424242); // Color gris oscuro en ARGB
            tvContent.setEllipsize(TextUtils.TruncateAt.END);
            tvContent.setMaxLines(3);
            inner.addView(tvContent);

            // Devolvemos el ViewHolder con referencias a los TextView y el listener de clics
            return new NoteVH(card, tvTitle, tvContent, listener);
        }

        @Override
        public void onBindViewHolder(@NonNull NoteVH holder, int position) {
            // Tomamos el elemento en 'position' y asignamos sus valores a las vistas
            Note note = data.get(position);
            holder.tvTitle.setText(TextUtils.isEmpty(note.title) ? "(Sin título)" : note.title);
            holder.tvContent.setText(TextUtils.isEmpty(note.content) ? "(Sin contenido)" : note.content);
        }

        @Override
        public int getItemCount() {
            // Cantidad total de items que debe dibujar el RecyclerView
            return data.size();
        }

        // ViewHolder: mantiene referencias a las vistas de cada ítem para reuso eficiente
        static class NoteVH extends RecyclerView.ViewHolder {
            TextView tvTitle, tvContent; // Referencias a las vistas de texto

            NoteVH(@NonNull View itemView,
                   TextView tvTitle,
                   TextView tvContent,
                   OnNoteInteraction listener) {
                super(itemView);
                this.tvTitle = tvTitle;
                this.tvContent = tvContent;

                // Click corto sobre toda la tarjeta: editar
                itemView.setOnClickListener(v -> {
                    if (listener != null) listener.onClick(getBindingAdapterPosition());
                });

                // Click largo: eliminar (devuelve true para indicar que el evento se consumió)
                itemView.setOnLongClickListener(v -> {
                    if (listener != null) listener.onLongClick(getBindingAdapterPosition());
                    return true;
                });
            }
        }
    }
}
