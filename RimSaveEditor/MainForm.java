package RimSaveEditor;

import RimSaveEditor.RimObjects.*;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.*;

public class MainForm extends JDialog {
    private File saveFile;

    private RimData rimData;
    private RimColonist selectedColotist;

    private JPanel contentPane;
    private JTextField tf_saveFilePath;
    private JButton btn_BrowseAndLoad;
    private JTextField tf_gameVersion;
    private JTextField tf_colonyName;
    private JTextField tf_faction;

    private JList list_Colonists;
    private DefaultListModel dlm_Colonists = new DefaultListModel();
    private JTextField tf_col_ID;
    private JTextField tf_col_Def;
    private JTextField tf_col_Lastname;
    private JTextField tf_col_Firstname;
    private JTextField tf_col_Nickname;
    private JComboBox cb_col_Sex;
    private JSpinner sp_col_Age;

    private JComboBox cb_col_Childhood;
    private JComboBox cb_col_Adulthood;

    private JComboBox cb_col_Trait1;
    private JComboBox cb_col_Trait2;

    private JTextArea ta_Debug;
    private JTabbedPane tp_Colonist;
    private JPanel tp_Colonist_General;
    private JPanel tp_Colonist_Skills;
    private JPanel tp_Colonist_Weapons;
    private JPanel tp_Colonist_Apparels;

    private JSpinner sp_col_skillConstruction;
    private JSpinner sp_col_skillGrowing;
    private JSpinner sp_col_skillResearch;
    private JSpinner sp_col_skillMining;
    private JSpinner sp_col_skillShooting;
    private JSpinner sp_col_skillMelee;
    private JSpinner sp_col_skillSocial;
    private JSpinner sp_col_skillCooking;
    private JSpinner sp_col_skillMedicine;
    private JSpinner sp_col_skillArtistic;
    private JSpinner sp_col_skillCrafting;
    private JSpinner sp_col_skillAnimals;
    private JComboBox cb_col_skillConstruction_Passion;
    private JComboBox cb_col_skillGrowing_Passion;
    private JComboBox cb_col_skillResearch_Passion;
    private JComboBox cb_col_skillMining_Passion;
    private JComboBox cb_col_skillShooting_Passion;
    private JComboBox cb_col_skillMelee_Passion;
    private JComboBox cb_col_skillSocial_Passion;
    private JComboBox cb_col_skillCooking_Passion;
    private JComboBox cb_col_skillMedicine_Passion;
    private JComboBox cb_col_skillArtistic_Passion;
    private JComboBox cb_col_skillCrafting_Passion;
    private JComboBox cb_col_skillAnimals_Passion;

    private JComboBox cb_col_Weapon_name;
    private JComboBox cb_col_Weapon_Quality;
    private JSpinner sp_col_Weapon_Health;

    private JComboBox cb_col_Apparel_name;
    private JComboBox cb_col_Apparel_quality;
    private JComboBox cb_col_Apparel_stuff;
    private JSpinner sp_col_Apparel_health;

    private JComboBox cb_col_Apparel_slot;
    private JComboBox cb_col_Weapon_slot;
    private JButton btn_col_Apparel_slot_add;
    private JButton btn_col_Apparel_slot_del;

    private JButton btn_Apparels_save;
    private JButton btn_Weapons_save;
    private JButton btn_Skills_save;
    private JButton btn_General_save;
    private JButton btn_SaveToFile;
    private JButton btn_reloadFile;
    private JButton btn_Colonists_heal;
    private JButton btn_Colonists_remNeeds;
    private JLabel lb_col_Apparel_colSlots;
    private JLabel lb_col_Weapon_colSlots;

    private ListCellRenderer cbBackstoryRenderer = new DefaultListCellRenderer() {
        @Override
        public Component getListCellRendererComponent(JList<?> list,
                                                      Object value, int index, boolean isSelected,
                                                      boolean cellHasFocus) {
            if (value instanceof RimBackstory) {
                setToolTipText(((RimBackstory) value).toolTip.toString());
                value = ((RimBackstory) value).toString();
            } else {
                setToolTipText(null);
            }
            ToolTipManager.sharedInstance().setDismissDelay(30000);
            Component result = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            return result;
        }

    };

    private ListCellRenderer cbTraitRenderer = new DefaultListCellRenderer() {
        @Override
        public Component getListCellRendererComponent(JList<?> list,
                                                      Object value, int index, boolean isSelected,
                                                      boolean cellHasFocus) {
            if (value instanceof RimTrait) {
                setToolTipText(((RimTrait) value).toolTip.toString());
                value = ((RimTrait) value).toString();
            } else {
                setToolTipText(null);
            }
            ToolTipManager.sharedInstance().setDismissDelay(30000);
            Component result = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            return result;
        }

    };

    private Map<String, JSpinner> skillFieldList = new HashMap<>();
    private Map<String, JComboBox> skillPassionFieldList = new HashMap<>();
    {
        skillFieldList.put("Construction", sp_col_skillConstruction);
        skillFieldList.put("Growing",  sp_col_skillGrowing);
        skillFieldList.put("Intellectual", sp_col_skillResearch);
        skillFieldList.put("Mining", sp_col_skillMining);
        skillFieldList.put("Shooting", sp_col_skillShooting);
        skillFieldList.put("Melee", sp_col_skillMelee);
        skillFieldList.put("Social", sp_col_skillSocial);
        skillFieldList.put("Cooking", sp_col_skillCooking);
        skillFieldList.put("Medicine", sp_col_skillMedicine);
        skillFieldList.put("Artistic", sp_col_skillArtistic);
        skillFieldList.put("Crafting", sp_col_skillCrafting);
        skillFieldList.put("Animals", sp_col_skillAnimals);

        skillPassionFieldList.put("Construction", cb_col_skillConstruction_Passion);
        skillPassionFieldList.put("Growing", cb_col_skillGrowing_Passion);
        skillPassionFieldList.put("Intellectual", cb_col_skillResearch_Passion);
        skillPassionFieldList.put("Mining", cb_col_skillMining_Passion);
        skillPassionFieldList.put("Shooting", cb_col_skillShooting_Passion);
        skillPassionFieldList.put("Melee", cb_col_skillMelee_Passion);
        skillPassionFieldList.put("Social", cb_col_skillSocial_Passion);
        skillPassionFieldList.put("Cooking", cb_col_skillCooking_Passion);
        skillPassionFieldList.put("Medicine", cb_col_skillMedicine_Passion);
        skillPassionFieldList.put("Artistic", cb_col_skillArtistic_Passion);
        skillPassionFieldList.put("Crafting", cb_col_skillCrafting_Passion);
        skillPassionFieldList.put("Animals", cb_col_skillAnimals_Passion);
    }

    private void onClose() {
        dispose();
    }

    public static void main(String[] args) {
        MainForm dialog = new MainForm();
        dialog.setTitle("RimWorld Save Editor");
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }

    public void showLogMsg() {
        for(String str: Logger.messages) {
            ta_Debug.append(str + "\n");
        }
        Logger.clear();
    }

    public MainForm() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(btn_BrowseAndLoad);
        tp_Colonist.setEnabled(false);

        // call onClose() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onClose();
            }
        });

        // call onClose() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onClose();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        btn_BrowseAndLoad.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileopen = new JFileChooser();
                File defaultDir = new File(System.getProperty("user.home") + "\\AppData\\LocalLow\\Ludeon Studios\\RimWorld by Ludeon Studios\\Saves");
                if(defaultDir.exists() && defaultDir.isDirectory()) {
                    fileopen.setCurrentDirectory(defaultDir);
                }
                fileopen.setFileFilter(new FileNameExtensionFilter("RimWorld Save Files", "rws"));
                int ret = fileopen.showDialog(null, "Open");
                if (ret == JFileChooser.APPROVE_OPTION) {
                    saveFile = fileopen.getSelectedFile();
                    Logger.log(0, "Save File selected");

                    rimData = new RimData();
                    if(rimData.loadSaveGameInfo(saveFile.toString())) {
                        tf_saveFilePath.setText(saveFile.toString());
                        tf_gameVersion.setText(rimData.gameVersion);
                        tf_colonyName.setText(rimData.playerColonyName);
                        tf_faction.setText(rimData.playerFaction);

                        rimData.loadBackstoriesDef();
                        rimData.loadTraitsDef();
                        rimData.loadApparelsDef();
                        rimData.loadWeaponsDef();

                        rimData.clearColonistData();
                        rimData.loadColonists();

                        dlm_Colonists.removeAllElements();
                        list_Colonists.setModel(dlm_Colonists);
                        for(RimColonist colonist: rimData.colonists) {
                            dlm_Colonists.addElement(colonist);
                        }

                        list_Colonists.setEnabled(true);
                        btn_reloadFile.setEnabled(true);
                        btn_Colonists_heal.setEnabled(true);
                        btn_Colonists_remNeeds.setEnabled(true);

                        initUIComponents();
                        setResizable(false);
                        pack();
                    }
                }
                else {
                    Logger.log(2, "Save file cannot open");
                }
                showLogMsg();
            }
        });

        list_Colonists.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if(e.getValueIsAdjusting()) {
                    showColonistInfo((RimColonist)list_Colonists.getSelectedValue());
                }
                showLogMsg();
            }
        });

        cb_col_Apparel_slot.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if(cb_col_Apparel_slot.getItemCount() > 0) {
                    lb_col_Apparel_colSlots.setText(String.valueOf(cb_col_Apparel_slot.getItemCount()));
                    if (cb_col_Apparel_slot.getSelectedItem() == null) return;
                    RimApparel selectedApparel = (RimApparel) cb_col_Apparel_slot.getSelectedItem();
                    RimApparel colonistApparel = selectedColotist.getApparelItemByDef(selectedApparel.def);

                    cb_col_Apparel_name.setSelectedItem(selectedApparel);
                    cb_col_Apparel_quality.setSelectedItem(colonistApparel.quality);
                    cb_col_Apparel_stuff.setSelectedItem(rimData.clothStuffTypes.indexOf(colonistApparel.stuff));
                    sp_col_Apparel_health.setValue(colonistApparel.health);

                    cb_col_Apparel_name.setEnabled(true);
                    cb_col_Apparel_quality.setEnabled(true);
                    cb_col_Apparel_stuff.setEnabled(true);
                    sp_col_Apparel_health.setEnabled(true);
                    btn_col_Apparel_slot_del.setEnabled(true);
                }
                else  {
                    btn_col_Apparel_slot_add.setEnabled(true);
                    btn_col_Apparel_slot_del.setEnabled(false);
                    cb_col_Apparel_name.setEnabled(false);
                    cb_col_Apparel_quality.setEnabled(false);
                    cb_col_Apparel_stuff.setEnabled(false);
                    sp_col_Apparel_health.setEnabled(false);
                }
            }
        });

        btn_col_Apparel_slot_add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cb_col_Apparel_name.setSelectedIndex(-1);
                cb_col_Apparel_quality.setSelectedItem(RimItemQuality.Legendary);
                cb_col_Apparel_stuff.setSelectedIndex(0);
                sp_col_Apparel_health.setValue(1000);

                cb_col_Apparel_slot.setEnabled(false);
                btn_col_Apparel_slot_del.setEnabled(false);
                list_Colonists.setEnabled(false);
                btn_col_Apparel_slot_add.setEnabled(false);

                cb_col_Apparel_name.setEnabled(true);
                cb_col_Apparel_quality.setEnabled(true);
                cb_col_Apparel_stuff.setEnabled(true);
                sp_col_Apparel_health.setEnabled(true);
            }
        });

        btn_col_Apparel_slot_del.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RimApparel selectedApparel = (RimApparel) cb_col_Apparel_slot.getSelectedItem();
                cb_col_Apparel_slot.removeItem(selectedApparel);

                for(RimApparel apparel: selectedColotist.apparels) {
                    if(apparel.def.equals(selectedApparel.def)) {
                        selectedColotist.apparels.remove(apparel);
                        break;
                    }
                }
            }
        });

        cb_col_Weapon_slot.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(cb_col_Weapon_slot.getSelectedItem() == null) return;
                RimWeapon selectedWeapon = (RimWeapon) cb_col_Weapon_slot.getSelectedItem();
                lb_col_Weapon_colSlots.setText(String.valueOf(cb_col_Weapon_slot.getItemCount()));

                if(selectedWeapon != null) {
                    int k = rimData.getWeaponIndexByDef(selectedWeapon.def);
                    if(k != -1)
                        cb_col_Weapon_name.setSelectedIndex(k);
                    else cb_col_Weapon_name.setSelectedIndex(0);

                    RimWeapon colonistWeapon = selectedColotist.getWeaponItemByDef(selectedWeapon.def);

                    cb_col_Weapon_Quality.setSelectedItem(colonistWeapon.quality);
                    sp_col_Weapon_Health.setValue(colonistWeapon.health);

                    cb_col_Weapon_name.setEnabled(true);
                    cb_col_Weapon_Quality.setEnabled(true);
                    sp_col_Weapon_Health.setEnabled(true);
                }
            }
        });

        btn_Apparels_save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!btn_col_Apparel_slot_add.isEnabled()) {
                    RimApparel item = new RimApparel();
                    item.def = ((RimApparel)cb_col_Apparel_name.getSelectedItem()).def;
                    item.id = item.def + (int)(Math.random()*100);
                    item.quality = RimItemQuality.valueOf(cb_col_Apparel_quality.getSelectedItem().toString());
                    item.stuff = cb_col_Apparel_stuff.getSelectedItem().toString();
                    item.health = Integer.parseInt(sp_col_Apparel_health.getValue().toString());
                    item.defLink = rimData.getApparelItemByDef(item.def);

                    selectedColotist.apparels.add(item);

                    cb_col_Apparel_slot.addItem(item.defLink);
                    cb_col_Apparel_slot.setSelectedItem(item.defLink);

                    btn_col_Apparel_slot_add.setEnabled(true);
                    btn_col_Apparel_slot_del.setEnabled(true);
                    cb_col_Apparel_slot.setEnabled(true);
                    list_Colonists.setEnabled(true);
                }

                RimApparel newApparel = (RimApparel) cb_col_Apparel_name.getSelectedItem();
                RimApparel currentApparel = (RimApparel) cb_col_Apparel_slot.getSelectedItem();
                RimApparel colonistApparel = selectedColotist.getApparelItemByDef(currentApparel.def);

                colonistApparel.setQuality(cb_col_Apparel_quality.getSelectedItem().toString());
                colonistApparel.stuff = cb_col_Apparel_stuff.getSelectedItem().toString();
                colonistApparel.health = Integer.parseInt(sp_col_Apparel_health.getValue().toString());

                if(!currentApparel.def.equals(newApparel.def)) {
                    colonistApparel.def = newApparel.def;
                    colonistApparel.id = colonistApparel.def + (int) (Math.random() * 100);
                    colonistApparel.defLink = newApparel;

                    cb_col_Apparel_slot.removeItem(currentApparel);
                    cb_col_Apparel_slot.addItem(newApparel);
                    cb_col_Apparel_slot.setSelectedItem(newApparel);
                }

                Logger.log(0, "Apparels data saved");
                showLogMsg();
            }
        });

        btn_Weapons_save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RimWeapon newWeapon = (RimWeapon) cb_col_Weapon_name.getSelectedItem();
                RimWeapon currentWeapon = (RimWeapon) cb_col_Weapon_slot.getSelectedItem();
                RimWeapon colonistWeapon;
                if(currentWeapon != null)
                    colonistWeapon = selectedColotist.getWeaponItemByDef(currentWeapon.def);
                else {
                    colonistWeapon = new RimWeapon();
                    selectedColotist.weapons.add(colonistWeapon);
                }

                colonistWeapon.setQuality(cb_col_Weapon_Quality.getSelectedItem().toString());
                colonistWeapon.health = Integer.parseInt(sp_col_Weapon_Health.getValue().toString());

                if(currentWeapon == null || !currentWeapon.def.equals(newWeapon.def)) {
                    colonistWeapon.def = newWeapon.def;
                    colonistWeapon.id = colonistWeapon.def + (int) (Math.random() * 100);
                    colonistWeapon.defLink = newWeapon;

                    if(currentWeapon != null)
                        cb_col_Weapon_slot.removeItem(currentWeapon);
                    cb_col_Weapon_slot.addItem(newWeapon);
                    cb_col_Weapon_slot.setSelectedItem(newWeapon);
                }
                Logger.log(0, "Waepons data saved");

                showLogMsg();
            }
        });

        btn_Skills_save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for(Map.Entry<String, RimSkill> skill: selectedColotist.skills.entrySet()) {
                    JSpinner spinner = skillFieldList.get(skill.getKey());
                    JComboBox comboBox = skillPassionFieldList.get(skill.getKey());
                    skill.getValue().level = (int)spinner.getValue();
                    skill.getValue().setPassion(comboBox.getSelectedItem().toString());
                }
                Logger.log(0, "Skills data saved");

                showLogMsg();
            }
        });

        btn_General_save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedColotist.firstName = tf_col_Firstname.getText();
                selectedColotist.nickName = tf_col_Nickname.getText();
                selectedColotist.lastName = tf_col_Lastname.getText();

                selectedColotist.setSex(cb_col_Sex.getSelectedItem().toString());
                selectedColotist.setAge(sp_col_Age.getValue().toString());

                RimBackstory childhood = (RimBackstory)cb_col_Childhood.getSelectedItem();
                RimBackstory adulthood = (RimBackstory)cb_col_Adulthood.getSelectedItem();
                selectedColotist.childhood = childhood.def;
                selectedColotist.adulthood = adulthood.def;

                int k = 0;
                k = cb_col_Trait1.getSelectedIndex();
                RimTrait trait1 = rimData.traits.get(k);
                k = cb_col_Trait2.getSelectedIndex();
                RimTrait trait2 = rimData.traits.get(k);

                if(selectedColotist.traits.size() == 0)
                    selectedColotist.traits.add(new RimTrait());
                selectedColotist.traits.get(0).def = trait1.def;
                selectedColotist.traits.get(0).degree = trait1.degree;

                if(selectedColotist.traits.size() == 1)
                    selectedColotist.traits.add(new RimTrait());
                selectedColotist.traits.get(1).def = trait2.def;
                selectedColotist.traits.get(1).degree = trait2.degree;

                Logger.log(0, "General data saved");
                showLogMsg();
            }
        });

        btn_SaveToFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rimData.saveColonists();
                btn_Colonists_heal.setEnabled(true);
                btn_Colonists_remNeeds.setEnabled(true);
                Logger.log(0, "Data saved to file");
                showLogMsg();
            }
        });

        btn_reloadFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rimData.clearColonistData();
                rimData.loadColonists();

                dlm_Colonists.removeAllElements();
                for(RimColonist colonist: rimData.colonists) {
                    dlm_Colonists.addElement(colonist);
                }

                list_Colonists.setEnabled(true);

                btn_Colonists_heal.setEnabled(true);
                btn_Colonists_remNeeds.setEnabled(true);
                Logger.log(0, "Data loaded from file");
                showLogMsg();
            }
        });

        btn_Colonists_heal.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rimData.healAll = true;
                btn_Colonists_heal.setEnabled(false);
            }
        });

        btn_Colonists_remNeeds.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rimData.removeNeeds = true;
                btn_Colonists_remNeeds.setEnabled(false);
            }
        });
    }

    public void showColonistInfo(RimColonist colonist) {
        this.selectedColotist = colonist;

        tp_Colonist.setEnabled(false);

        tf_col_ID.setText(colonist.id);
        tf_col_Def.setText(colonist.def);

        // NAME
        tf_col_Firstname.setText(colonist.firstName);
        tf_col_Lastname.setText(colonist.lastName);
        tf_col_Nickname.setText(colonist.nickName);
        tf_col_Firstname.setEnabled(true);
        tf_col_Lastname.setEnabled(true);
        tf_col_Nickname.setEnabled(true);

        // SEX
        cb_col_Sex.setSelectedItem(colonist.sex);
        cb_col_Sex.setEnabled(true);

        // AGE
        sp_col_Age.setValue((int)colonist.getAge());
        sp_col_Age.setEnabled(true);

        // CHILDHOOD && ADULTHOOD (BACKSTORIES)
        {
            String name = colonist.childhood;
            int k = rimData.getBackstoryIndexByDef(name);
            if (k != -1) cb_col_Childhood.setSelectedIndex(k);
            else {
                if (!cb_col_Childhood.getItemAt(cb_col_Childhood.getItemCount() - 1).equals(name)) {
                    cb_col_Childhood.addItem(name);
                }
                cb_col_Childhood.setSelectedIndex(cb_col_Childhood.getItemCount() - 1);
            }
            cb_col_Childhood.setEnabled(true);
        }

        {
            String name = colonist.adulthood;
            int k = rimData.getBackstoryIndexByDef(name);
            if (k != -1) cb_col_Adulthood.setSelectedIndex(k);
            else {
                if (!cb_col_Adulthood.getItemAt(cb_col_Adulthood.getItemCount() - 1).equals(name)) {
                    cb_col_Adulthood.addItem(name);
                }
                cb_col_Adulthood.setSelectedIndex(cb_col_Adulthood.getItemCount() - 1);
            }
            cb_col_Adulthood.setEnabled(true);
        }


        // TRAITS
        cb_col_Trait1.setEnabled(true);
        if(colonist.traits.size() > 0) {
            int k = 0;
            if((k = rimData.getTraitIndexByDef(colonist.traits.get(0))) != -1)
                cb_col_Trait1.setSelectedIndex(k);
            else {
                cb_col_Trait1.addItem(colonist.traits.get(0));
                cb_col_Trait1.setSelectedItem(colonist.traits.get(0));
            }
        }
        cb_col_Trait2.setEnabled(true);
        if(colonist.traits.size() > 1) {
            int k = 0;
            if((k = rimData.getTraitIndexByDef(colonist.traits.get(1))) != -1)
                cb_col_Trait2.setSelectedIndex(k);
            else {
                cb_col_Trait2.addItem(colonist.traits.get(1));
                cb_col_Trait2.setSelectedItem(colonist.traits.get(1));
            }
        }

        // SKILLS
        for(Map.Entry<String, RimSkill> skill: colonist.skills.entrySet()){
            JSpinner level = this.skillFieldList.get(skill.getKey());
            JComboBox passion = this.skillPassionFieldList.get(skill.getKey());
            level.setValue(skill.getValue().level);
            passion.setSelectedItem(skill.getValue().passion);
        }

        // WEAPON
        cb_col_Weapon_slot.removeAllItems();
        //cb_col_Weapon_name.removeAllItems();
        cb_col_Weapon_Quality.setSelectedItem(0);
        sp_col_Weapon_Health.setValue(500);

        cb_col_Weapon_name.setEnabled(false);
        cb_col_Weapon_Quality.setEnabled(false);
        sp_col_Weapon_Health.setEnabled(false);
        lb_col_Weapon_colSlots.setText(String.valueOf(cb_col_Weapon_slot.getItemCount()));

        if(this.selectedColotist.weapons.size() > 0) {
            for (RimWeapon item : this.selectedColotist.weapons) {
                cb_col_Weapon_slot.addItem(item.defLink);
            }
            cb_col_Weapon_slot.setSelectedIndex(0);
        }
        else {
            cb_col_Weapon_slot.setSelectedIndex(-1);
            cb_col_Weapon_name.setSelectedIndex(-1);
            cb_col_Weapon_name.setEnabled(true);
            cb_col_Weapon_Quality.setEnabled(true);
            sp_col_Weapon_Health.setEnabled(true);
        }

        // APPARELS
        cb_col_Apparel_slot.removeAllItems();
        if(colonist.apparels.size() > 0) {
            for (RimApparel item : colonist.apparels) {
                cb_col_Apparel_slot.addItem(item.defLink);
            }
            cb_col_Apparel_slot.setSelectedIndex(0);
        }
        else {
            cb_col_Apparel_slot.setSelectedIndex(-1);
        }
        btn_col_Apparel_slot_del.setEnabled(true);
        lb_col_Apparel_colSlots.setText(String.valueOf(cb_col_Apparel_slot.getItemCount()));

        tp_Colonist.setEnabled(true);
        btn_General_save.setEnabled(true);
        btn_SaveToFile.setEnabled(true);
    }

    public void initUIComponents() {

        btn_General_save.setEnabled(false);
        btn_SaveToFile.setEnabled(false);

        // NAME
        tf_col_Firstname.setEnabled(false);
        tf_col_Lastname.setEnabled(false);
        tf_col_Nickname.setEnabled(false);

        // AGE
        sp_col_Age.setEnabled(false);

        // SEX
        cb_col_Sex.removeAllItems();
        cb_col_Sex.setEnabled(false);
        for(RimGender obj: RimGender.values()) {
            cb_col_Sex.addItem(obj);
        }

        // CHILDHOOD && ADULTHOOD
        cb_col_Childhood.removeAllItems();
        cb_col_Adulthood.removeAllItems();
        cb_col_Childhood.setEnabled(false);
        cb_col_Adulthood.setEnabled(false);
        for(RimBackstory backstory: rimData.backstories) {
            cb_col_Childhood.addItem(backstory);
            cb_col_Adulthood.addItem(backstory);
        }
        cb_col_Childhood.setRenderer(cbBackstoryRenderer);
        cb_col_Adulthood.setRenderer(cbBackstoryRenderer);

        // TRAITS
        cb_col_Trait1.removeAllItems();
        cb_col_Trait2.removeAllItems();
        cb_col_Trait1.setEnabled(false);
        cb_col_Trait2.setEnabled(false);
        for(RimTrait trait: rimData.traits) {
            cb_col_Trait1.addItem(trait);
            cb_col_Trait2.addItem(trait);
        }
        cb_col_Trait1.setRenderer(cbTraitRenderer);
        cb_col_Trait2.setRenderer(cbTraitRenderer);

        // SKILLS
        for(Map.Entry<String, JSpinner> level: skillFieldList.entrySet()){
            JSpinner spinner = level.getValue();
            spinner.setModel(new SpinnerNumberModel(0, 0, 20, 1));
        }
        for(Map.Entry<String, JComboBox> passion: skillPassionFieldList.entrySet()){
            JComboBox combobox = passion.getValue();
            combobox.removeAllItems();
            for(RimSkillPassion obj: RimSkillPassion.values()) {
                combobox.addItem(obj);
            }
            combobox.setSelectedIndex(0);
        }

        // WEAPONS
        cb_col_Weapon_slot.removeAllItems();
        cb_col_Weapon_name.removeAllItems();
        cb_col_Weapon_Quality.removeAllItems();
        for(RimWeapon weapon: rimData.weapons) {
            cb_col_Weapon_name.addItem(weapon);
        }

        sp_col_Weapon_Health.setModel(new SpinnerNumberModel(0, 0, 9999, 1));

        for(RimItemQuality obj: RimItemQuality.values()) {
            cb_col_Weapon_Quality.addItem(obj);
            cb_col_Apparel_quality.addItem(obj);
        }

        // APPARELS
        //cb_col_Apparel_name.removeAllItems();
        cb_col_Apparel_slot.removeAllItems();
        for(RimApparel apparel: rimData.apparels) {
            cb_col_Apparel_name.addItem(apparel);
        }
        for (String stuff: rimData.clothStuffTypes) {
            cb_col_Apparel_stuff.addItem(stuff);
        }
        sp_col_Apparel_health.setModel(new SpinnerNumberModel(0, 0, 9999, 1));

    }


}
