import React from 'react';
// eslint-disable-line

import MenuItem from 'app/shared/layout/menus/menu-item'; // eslint-disable-line

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/bot">
        Bot
      </MenuItem>
      <MenuItem icon="asterisk" to="/intent">
        Intent
      </MenuItem>
      <MenuItem icon="asterisk" to="/response-data">
        Response Data
      </MenuItem>
      <MenuItem icon="asterisk" to="/intent-entity">
        Intent Entity
      </MenuItem>
      <MenuItem icon="asterisk" to="/utterance">
        Utterance
      </MenuItem>
      <MenuItem icon="asterisk" to="/conversation">
        Conversation
      </MenuItem>
      <MenuItem icon="asterisk" to="/chat-message">
        Chat Message
      </MenuItem>
      <MenuItem icon="asterisk" to="/slot-value">
        Slot Value
      </MenuItem>
      <MenuItem icon="asterisk" to="/bot-training">
        Bot Training
      </MenuItem>
      <MenuItem icon="asterisk" to="/utterance-intent">
        Utterance Intent
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
